import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import {JVxeTypes,JVxeColumn} from '/@/components/jeecg/JVxeTable/types'
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '钓场名称',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '钓场首页图',
    align:"center",
    dataIndex: 'homeImage',
    customRender:render.renderImage,
   },
   {
    title: '钓场地址',
    align:"center",
    dataIndex: 'address'
   },
   {
    title: '钓位数量',
    align:"center",
    dataIndex: 'positionQuantity'
   },
   {
    title: '营业开始时间',
    align:"center",
    dataIndex: 'startTime'
   },
   {
    title: '营业结束时间',
    align:"center",
    dataIndex: 'endTime'
   },
   {
    title: '价格',
    align:"center",
    dataIndex: 'price'
   },
   {
    title: '钓场状态',
    align:"center",
    dataIndex: 'status_dictText'
   },
];

//子表表格配置
export const fcFishBoatColumns: JVxeColumn[] = [
    {
      title: '船号',
      key: 'boatNumber',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '状态',
      key: 'status',
      type: JVxeTypes.select,
      options:[],
      dictCode:"fish_boat_status",
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '载客量',
      key: 'capacity',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '备注',
      key: 'remark',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
  ]

// 高级查询数据
export const superQuerySchema = {
  name: {title: '钓场名称',order: 0,view: 'text', type: 'string',},
  homeImage: {title: '钓场首页图',order: 1,view: 'image', type: 'string',},
  address: {title: '钓场地址',order: 4,view: 'text', type: 'string',},
  positionQuantity: {title: '钓位数量',order: 6,view: 'number', type: 'number',},
  startTime: {title: '营业开始时间',order: 7,view: 'time', type: 'string',},
  endTime: {title: '营业结束时间',order: 8,view: 'time', type: 'string',},
  price: {title: '价格',order: 9,view: 'number', type: 'number',},
  status: {title: '钓场状态',order: 14,view: 'list', type: 'string',dictCode: 'fish_ground_status',},
  //子表高级查询
  fcFishBoat: {
    title: '钓场船只',
    view: 'table',
    fields: {
        boatNumber: {title: '船号',order: 0,view: 'text', type: 'string',},
        status: {title: '状态',order: 1,view: 'list', type: 'string',dictCode: 'fish_boat_status',},
        capacity: {title: '载客量',order: 2,view: 'number', type: 'number',},
        remark: {title: '备注',order: 3,view: 'text', type: 'string',},
    }
  },
};
