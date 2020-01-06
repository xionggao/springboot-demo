package com.xxy.sfl.pub.controller;

import com.xxy.sfl.pub.entity.SuperEntity;
import com.xxy.sfl.pub.service.ICommonService;
import com.xxy.sfl.pub.utils.JsonBackData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 通用的Restful控制器
 *
 * @author xg
 */
@MappedSuperclass
public class BaseRestController<S extends ICommonService<T>, T extends SuperEntity> {

    @Autowired
    private S service;

    @GetMapping("/findById")
    public JsonBackData findById(@RequestParam String id) {
        JsonBackData back = new JsonBackData();
        T result = service.findById(id);
        back.setBackData(result);
        back.setBackMsg("查询成功");
        return back;
    }

    @GetMapping("/findAll")
    public JsonBackData findAll(@RequestParam(required = false) Map<String, Object> condition) {
        JsonBackData back = new JsonBackData();
        List<T> result = service.findAll(condition);
        back.setBackData(result);
        back.setBackMsg("查询成功");
        return back;
    }

    @GetMapping("/findPage")
    public JsonBackData findPage(@RequestParam(required = false) String pageNumber,
                                 @RequestParam(required = false) String pageSize,
                                 @RequestParam(required = false) Map<String, Object> condition) {
        JsonBackData back = new JsonBackData();
        Pageable page = null;
        if (StringUtils.hasText(pageNumber) && StringUtils.hasText(pageSize)) {
            page = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize));
        }
        Page<T> result = service.findPage(page, condition);
        back.setBackData(result);
        back.setBackMsg("查询成功");
        return back;
    }

    @PostMapping("/save")
    public JsonBackData save(@RequestBody T t) {
        JsonBackData back = new JsonBackData();
        t = service.saveOrUpdate(t);
        back.setBackData(t);
        back.setBackMsg("保存成功");
        return back;
    }

    @GetMapping("/deleteById")
    public JsonBackData deleteById(@RequestParam Serializable id) {
        JsonBackData back = new JsonBackData();
        service.deleteById(id);
        back.setBackMsg("删除成功");
        return back;
    }

    @GetMapping("/deleteByIds")
    public JsonBackData deleteByIds(@RequestParam Serializable... ids) {
        JsonBackData back = new JsonBackData();
        service.deleteByIds(ids);
        back.setBackMsg("删除成功");
        return back;
    }

    @GetMapping("/deleteAll")
    public JsonBackData deleteAll() {
        JsonBackData back = new JsonBackData();
        service.deleteAll();
        back.setBackMsg("删除成功");
        return back;
    }
}
