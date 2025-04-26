import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '预约用户昵称',
    align: "center",
    dataIndex: 'realname'
  },
  {
    title: '钓场名称',
    align: "center",
    dataIndex: 'groundName'
  },
  {
    title: '预约日期',
    align: "center",
    dataIndex: 'date',
    customRender:({text}) =>{
      text = !text ? "" : (text.length > 10 ? text.substr(0,10) : text);
      return text;
    },
  },
  {
    title: '船号',
    align: "center",
    dataIndex: 'boatNumber'
  },
  {
    title: '预约用户手机号',
    align: "center",
    dataIndex: 'phone'
  },
  {
    title: '预约票价',
    align: "center",
    dataIndex: 'fare'
  },
  {
    title: '预约状态',
    align: "center",
    dataIndex: 'status_dictText'
  },
];

// 高级查询数据
export const superQuerySchema = {
  realname: {title: '预约用户昵称',order: 0,view: 'popup', type: 'string',code: 'fc_fish_user_report', orgFields: 'realname', destFields: 'realname', popupMulti: false,},
  groundName: {title: '钓场名称',order: 1,view: 'text', type: 'string',},
  date: {title: '预约日期',order: 2,view: 'date', type: 'string',},
  boatNumber: {title: '船号',order: 3,view: 'popup', type: 'string',code: 'fc_fish_boat_report', orgFields: 'boat_number', destFields: 'boatNumber', popupMulti: false,},
  phone: {title: '预约用户手机号',order: 4,view: 'text', type: 'string',},
  fare: {title: '预约票价',order: 5,view: 'number', type: 'number',},
  status: {title: '预约状态',order: 6,view: 'list', type: 'string',dictCode: 'fish_order_status',},
};
