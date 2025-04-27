package org.jeecg.modules.fucci.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.fucci.pojo.vo.FucciOrderVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lhf
 * @date 2025-04-27
 * @describe
 */
public interface IFucciOrderService {

    /**
     * 分页查询用户预约信息列表数据
     *
     * @param request  请求信息
     * @param pageNo   页码
     * @param pageSize 每页查询数量
     * @return 用户预约信息列表数据
     */
    IPage<FucciOrderVO> list(HttpServletRequest request, Integer pageNo, Integer pageSize);

    /**
     * 查询预约完成详情信息
     *
     * @param id 预约id
     * @return 预约完成详情信息
     */
    FucciOrderVO details(String id);

}
