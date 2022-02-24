package com.ryuzu.server.controller;


import com.ryuzu.server.domain.Position;
import com.ryuzu.server.domain.RespBean;
import com.ryuzu.server.service.IPositionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {

    @Resource
    private IPositionService positionService;

    @ApiOperation(value = "查询所有职位信息")
    @GetMapping("/")
    public List<Position> selectPositionList() {
        List<Position> list = positionService.list();
        return list;
    }

    @ApiOperation(value = "添加职位信息")
    @PostMapping("/")
    public RespBean addPosition(@RequestBody Position position) {
        if (positionService.save(position)) {
            return RespBean.success("添加成功!");
        } else {
            return RespBean.error("添加失败!");
        }
    }

    @ApiOperation(value = "更新职员信息")
    @PutMapping("/")
    public RespBean updatePosition(@RequestBody Position position) {
        if (positionService.updateById(position)) {
            return RespBean.success("更新成功!");
        } else {
            return RespBean.error("更新失败!");
        }
    }

    @ApiOperation(value = "删除职员信息")
    @DeleteMapping("/{id}")
    public RespBean deletePosition(@PathVariable Integer id) {
        if (positionService.removeById(id)) {
            return RespBean.success("删除成功!");
        } else {
            return RespBean.error("删除失败!");
        }

    }

    @ApiOperation(value = "删除多条职员信息")
    @DeleteMapping("/")
    public RespBean deletePositionByIds(Integer[] ids) {
        if (positionService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("删除多条成功!");
        }else{
            return RespBean.error("删除多条失败!");
        }
    }
}
