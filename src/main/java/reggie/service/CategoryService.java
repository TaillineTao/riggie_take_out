package reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import reggie.entity.Category;

public interface CategoryService extends IService<Category> {


    void remove(Long id);
}
