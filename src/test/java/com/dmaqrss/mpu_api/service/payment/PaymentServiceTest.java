package com.dmaqrss.mpu_api.service.payment;

import com.dmaqrss.mpu_api.dto.payment.PaymentResponseDTO;
import com.dmaqrss.mpu_api.exception.BusinessException;
import com.dmaqrss.mpu_api.model.Order;
import com.dmaqrss.mpu_api.model.Payment;
import com.dmaqrss.mpu_api.model.User;
import com.dmaqrss.mpu_api.model.roles.OrderStatusRole;
import com.dmaqrss.mpu_api.model.roles.PaymentMethod;
import com.dmaqrss.mpu_api.model.roles.PaymentStatus;
import com.dmaqrss.mpu_api.publisher.PaymentEmailPublisher;
import com.dmaqrss.mpu_api.repository.OrderRepository;
import com.dmaqrss.mpu_api.repository.PaymentRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRespository paymentRespository;

    @Mock
    private PaymentEmailPublisher paymentEmailPublisher;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldCreatePaymentSuccessfully() {
        User user = new User();
        user.setId(1L);

        Order order = new Order();
        order.setId(10L);
        order.setUser(user);
        order.setStatus(OrderStatusRole.PENDING);
        order.setTotal(BigDecimal.valueOf(100));

        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

        PaymentResponseDTO response =
                paymentService.createPayment(10L, PaymentMethod.PIX, user);

        assertNotNull(response);
        verify(paymentRespository).save(any(Payment.class));
        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> paymentService.createPayment(1L, PaymentMethod.PIX, new User()));
        verify(paymentRespository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        User owner = new User();
        owner.setId(1L);

        User anotherUser = new User();
        anotherUser.setId(2L);

        Order order = new Order();
        order.setUser(owner);
        order.setStatus(OrderStatusRole.PENDING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BusinessException.class,
                () -> paymentService.createPayment(1L, PaymentMethod.PIX, anotherUser));
        verify(paymentRespository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldConfirmPaymentSuccessfully() {
        User user = new User();
        user.setEmail("teste@email.com");

        Order order = new Order();
        order.setUser(user);

        Payment payment = new Payment();
        payment.setId(5L);
        payment.setStatus(PaymentStatus.CREATED);
        payment.setOrder(order);

        when(paymentRespository.findById(5L)).thenReturn(Optional.of(payment));

        PaymentResponseDTO response = paymentService.confirmPayment(5L);

        assertEquals(PaymentStatus.CONFIRMED, payment.getStatus());
        assertEquals(OrderStatusRole.PAID, order.getStatus());

        verify(paymentRespository).save(payment);
        verify(orderRepository).save(order);
        verify(paymentEmailPublisher).paymentConfirmedEmail(any());
    }

    @Test
    void shouldThrowExceptionWhenPaymentAlreadyConfirmed() {
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.CONFIRMED);

        when(paymentRespository.findById(1L)).thenReturn(Optional.of(payment));

        assertThrows(BusinessException.class,
                () -> paymentService.confirmPayment(1L));
    }

    @Test
    void shouldFailPaymentSuccessfully() {
        User user = new User();
        user.setEmail("teste@email.com");

        Order order = new Order();
        order.setUser(user);

        Payment payment = new Payment();
        payment.setId(7L);
        payment.setStatus(PaymentStatus.CREATED);
        payment.setOrder(order);

        when(paymentRespository.findById(7L)).thenReturn(Optional.of(payment));

        PaymentResponseDTO response = paymentService.failPayment(7L);

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        verify(paymentRespository).save(payment);
        verify(paymentEmailPublisher).paymentFailedEmail(any());
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotFound() {
        when(paymentRespository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> paymentService.confirmPayment(99L));
        verify(paymentRespository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }
}
