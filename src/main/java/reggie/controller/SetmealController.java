package reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import reggie.common.R;
import reggie.dto.DishDto;
import reggie.dto.SetmealDto;
import reggie.entity.Category;
import reggie.entity.Setmeal;
import reggie.service.CategoryService;
import reggie.service.SetmealDishService;
import reggie.service.SetmealService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

     @Autowired
    private SetmealDishService setmealDishService;

     @PostMapping
     @CacheEvict(value = "setmealCache", allEntries = true)
     public R<String> save(@RequestBody SetmealDto setmealDto) {
         log.info("套餐信息{}",setmealDto);
         setmealService.saveWithDish(setmealDto);
         return R.success("新增套餐成功" );
     }

     @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
         Page<Setmeal> pageInfo = new Page<>(page, pageSize);
         Page<SetmealDto> dtoPage = new Page<>();

         LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
         queryWrapper.like(name != null, Setmeal::getName,name);
         queryWrapper.orderByDesc(Setmeal::getUpdateTime);

         setmealService.page(pageInfo,queryWrapper);

         BeanUtils.copyProperties(pageInfo,dtoPage,"records");
         List<Setmeal> records = pageInfo.getRecords();
         List<SetmealDto> list = records.stream().map((item) -> {
             SetmealDto setmealDto = new SetmealDto();
             BeanUtils.copyProperties(item, setmealDto);
             Long categoryId = item.getCategoryId();
             Category category = categoryService.getById(categoryId);
             if (category != null) {
                 String categoryName = category.getName();
                 setmealDto.setCategoryName(categoryName);
             }
             return setmealDto;
         }).collect(Collectors.toList());

         dtoPage.setRecords(list);
         return R.success(dtoPage);
     }


     @DeleteMapping
     @CacheEvict(value = "setmealCache", allEntries = true)
     public R<String> delete(@RequestParam List<Long> ids) {
         log.info("删除ID为{}的套餐",ids);
         setmealService.removeWithDish(ids);
         return R.success("套餐数据删除成功！");
     }


     @GetMapping("/list")
     @Cacheable(value = "setmaelCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
     public R<List<Setmeal>> list(Setmeal setmeal) {
         LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
         queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId,setmeal.getCategoryId());
         queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus,setmeal.getStatus());
         queryWrapper.orderByDesc(Setmeal::getUpdateTime);

         List<Setmeal> list = setmealService.list(queryWrapper);

         return R.success(list);
     }











}
