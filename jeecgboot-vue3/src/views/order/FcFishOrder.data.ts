import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
// 列表数据
export const columns: BasicColumn[] = [
  {
    title: '预约订单ID',
    align: "center",
    dataIndex: 'id',
    width: 180,
  },
  {
    title: '小程序用户ID',
    align: "center",
    dataIndex: 'username',
    width: 180,
  },
  {
    title: '小程序用户昵称',
    align: "center",
    dataIndex: 'realname',
    width: 120,
  },
  {
    title: '钓场名称',
    align: "center",
    dataIndex: 'groundName',
    width: 120,
  },
  {
    title: '预约日期',
    align: "center",
    dataIndex: 'date',
    width: 120,
    customRender:({text}) =>{
      text = !text ? "" : (text.length > 10 ? text.substr(0,10) : text);
      return text;
    },
  },
  {
    title: '船号',
    align: "center",
    dataIndex: 'boatNumber',
    width: 90,
  },
  {
    title: '预约用户姓名',
    align: "center",
    dataIndex: 'name',
    width: 120,
  },
  {
    title: '预约用户手机号',
    align: "center",
    dataIndex: 'phone',
    width: 130,
  },
  {
    title: '预约票价',
    align: "center",
    dataIndex: 'fare',
    width: 90,
  },
  {
    title: '预约状态',
    align: "center",
    dataIndex: 'status_dictText',
    width: 220,
  },
  {
    title: '创建日期',
    align: "center",
    dataIndex: 'createTime',
    width: 180,
  },
];

// 高级查询数据
export const superQuerySchema = {
  username: {title: '小程序用户ID',order: 0,view: 'text', type: 'string',},
  realname: {title: '小程序用户昵称',order: 1,view: 'popup', type: 'string',code: 'fc_fish_user_report', orgFields: 'realname', destFields: 'realname', popupMulti: false,},
  groundName: {title: '钓场名称',order: 2,view: 'text', type: 'string',},
  date: {title: '预约日期',order: 3,view: 'date', type: 'string',},
  boatNumber: {title: '船号',order: 4,view: 'popup', type: 'string',code: 'fc_fish_boat_report', orgFields: 'boat_number', destFields: 'boatNumber', popupMulti: false,},
  name: {title: '预约用户姓名',order: 5,view: 'text', type: 'string',},
  phone: {title: '预约用户手机号',order: 6,view: 'text', type: 'string',},
  fare: {title: '预约票价',order: 7,view: 'number', type: 'number',},
  status: {title: '预约状态',order: 8,view: 'list', type: 'string',dictCode: 'fish_order_status',},
  createTime: {title: '创建日期',order: 9,view: 'datetime', type: 'string',},
};
