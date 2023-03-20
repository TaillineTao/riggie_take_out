package reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import reggie.dto.DishDto;
import reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

   public void updateWithFlavor(DishDto dishDto);

   public  void deleteDishWithFlavors(List<Long> ids);
}
