package com.dmaqrss.mpu_api.service.payment;

import com.dmaqrss.mpu_api.dto.payment.PaymentEmailDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentRespository paymentRespository;

    @Autowired
    PaymentEmailPublisher paymentEmailPublisher;

    @Transactional
    public PaymentResponseDTO createPayment(Long id, PaymentMethod method, User user){
        Order order = validateOrder(id);
        if(!order.getUser().getId().equals(user.getId())) throw new BusinessException("somente o dono da compra pode solicitar o pagamento");

        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setAmount(order.getTotal());
        payment.setOrder(order);
        payment.setStatus(PaymentStatus.CREATED);
        payment.setMethod(method);
        paymentRespository.save(payment);

        order.addPayment(payment);
        orderRepository.save(order);

        return new PaymentResponseDTO(payment);

    }

    private Order validateOrder(Long id){
        Order order = orderRepository.findById(id).orElseThrow(() -> new BusinessException("Pedido não encontrado"));
        if(order.getStatus() == OrderStatusRole.CANCELED){
            throw new BusinessException("O pedido foi cancelado");
        }
        if(order.getStatus() == OrderStatusRole.PAID){
            throw new BusinessException("O pedido ja foi pago");
        }

        return order;
    }

    @Transactional
    public PaymentResponseDTO confirmPayment(Long id){
        Payment payment = validatePayment(id);
        payment.setConfirmedAt(LocalDateTime.now());
        payment.setStatus(PaymentStatus.CONFIRMED);
        paymentRespository.save(payment);

        Order order = payment.getOrder();
        order.setStatus(OrderStatusRole.PAID);
        orderRepository.save(order);

        paymentEmailPublisher.paymentConfirmedEmail(new PaymentEmailDTO(order.getUser().getEmail(), payment.getId()));

        return new PaymentResponseDTO(payment);
    }

    public Payment validatePayment(Long id){
        Payment payment = paymentRespository.findById(id).orElseThrow(() -> new BusinessException("pagamento nao achado"));
        if(payment.getStatus() == PaymentStatus.CONFIRMED){
            throw new BusinessException("o pagamento ja foi confirmado");
        }
        if(payment.getStatus() == PaymentStatus.FAILED){
            throw new BusinessException("o este pagamento esta invalido");
        }

        return payment;
    }

    @Transactional
    public PaymentResponseDTO failPayment(Long id){
        Payment payment = validatePayment(id);

        payment.setStatus(PaymentStatus.FAILED);
        paymentRespository.save(payment);

        paymentEmailPublisher.paymentFailedEmail(new PaymentEmailDTO(payment.getOrder().getUser().getEmail(), payment.getId()));

        return new PaymentResponseDTO(payment);
    }
}
