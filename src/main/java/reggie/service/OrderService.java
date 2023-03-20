package reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reggie.common.R;
import reggie.entity.Orders;

public interface OrderService extends IService<Orders> {


    public void submit(Orders orders);


}
