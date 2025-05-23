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
    title: '员工姓名',
    align: "center",
    dataIndex: 'name'
  },
  {
    title: '员工手机号',
    align: "center",
    dataIndex: 'phone'
  },
  {
    title: '钓场名称',
    align: "center",
    dataIndex: 'groundName'
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
  name: {title: '员工姓名',order: 2,view: 'text', type: 'string',},
  phone: {title: '员工手机号',order: 3,view: 'text', type: 'string',},
  groundName: {title: '钓场名称',order: 4,view: 'popup', type: 'string',code: 'fc_fish_ground_report', orgFields: 'name', destFields: 'groundName', popupMulti: false,},
  createTime: {title: '创建日期',order: 5,view: 'datetime', type: 'string',},
};
