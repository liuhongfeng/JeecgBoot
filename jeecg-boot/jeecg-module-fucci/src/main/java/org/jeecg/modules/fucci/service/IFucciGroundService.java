package org.jeecg.modules.fucci.service;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundDetailsVO;

/**
 * @author lhf
 * @date 2025-04-15
 * @describe
 */
public interface IFucciGroundService {

    /**
     * 查询钓场列表信息
     *
     * @return 钓场列表信息
     */
    JSONObject list();

    /**
     * 查询钓场详情信息
     *
     * @param id 钓场id
     * @return 钓场详情信息
     */
    FucciGroundDetailsVO details(String id);

}
