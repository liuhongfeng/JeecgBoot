import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '小程序用户ID',
    align: "center",
    dataIndex: 'username'
  },
  {
    title: '小程序用户昵称',
    align: "center",
    dataIndex: 'realname'
  },
  {
    title: '会员姓名',
    align: "center",
    dataIndex: 'name'
  },
  {
    title: '会员手机号',
    align: "center",
    dataIndex: 'phone'
  },
  {
    title: '会员次数',
    align: "center",
    dataIndex: 'count'
  },
  {
    title: '创建日期',
    align: "center",
    dataIndex: 'createTime'
  },
];

// 高级查询数据
export const superQuerySchema = {
  username: {title: '小程序用户ID',order: 0,view: 'text', type: 'string',},
  realname: {title: '小程序用户昵称',order: 1,view: 'popup', type: 'string',code: 'fc_fish_user_report', orgFields: 'realname', destFields: 'realname', popupMulti: false,},
  name: {title: '会员姓名',order: 2,view: 'text', type: 'string',},
  phone: {title: '会员手机号',order: 3,view: 'text', type: 'string',},
  count: {title: '会员次数',order: 6,view: 'number', type: 'number',},
  createTime: {title: '创建日期',order: 6,view: 'datetime', type: 'string',},
};
