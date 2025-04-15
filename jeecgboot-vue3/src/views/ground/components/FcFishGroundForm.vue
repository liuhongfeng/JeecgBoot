<template>
  <a-spin :spinning="loading">
    <JFormContainer :disabled="disabled">
      <template #detail>
        <a-form v-bind="formItemLayout" name="FcFishGroundForm" ref="formRef">
          <a-row>
						<a-col :span="24">
							<a-form-item label="钓场名称" v-bind="validateInfos.name" id="FcFishGroundForm-name" name="name">
								<a-input v-model:value="formData.name" placeholder="请输入钓场名称"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="钓场首页图" v-bind="validateInfos.homeImage" id="FcFishGroundForm-homeImage" name="homeImage">
								<j-image-upload :fileMax=1 v-model:value="formData.homeImage" ></j-image-upload>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="钓场详情图" v-bind="validateInfos.detailsImage" id="FcFishGroundForm-detailsImage" name="detailsImage">
								<j-image-upload :fileMax=4 v-model:value="formData.detailsImage" ></j-image-upload>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="钓场介绍" v-bind="validateInfos.introduce" id="FcFishGroundForm-introduce" name="introduce">
								<a-textarea v-model:value="formData.introduce" :rows="4" placeholder="请输入钓场介绍" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="钓场地址" v-bind="validateInfos.address" id="FcFishGroundForm-address" name="address">
								<a-input v-model:value="formData.address" placeholder="请输入钓场地址"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="联系电话" v-bind="validateInfos.phone" id="FcFishGroundForm-phone" name="phone">
								<a-input v-model:value="formData.phone" placeholder="请输入联系电话"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="钓位数量" v-bind="validateInfos.positionQuantity" id="FcFishGroundForm-positionQuantity" name="positionQuantity">
								<a-input-number v-model:value="formData.positionQuantity" placeholder="请输入钓位数量" style="width: 100%" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="营业开始时间" v-bind="validateInfos.startTime" id="FcFishGroundForm-startTime" name="startTime">
								<time-picker placeholder="请选择营业开始时间" value-format="HH:mm:ss"  v-model:value="formData.startTime" style="width: 100%"  allow-clear />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="营业结束时间" v-bind="validateInfos.endTime" id="FcFishGroundForm-endTime" name="endTime">
								<time-picker placeholder="请选择营业结束时间" value-format="HH:mm:ss"  v-model:value="formData.endTime" style="width: 100%"  allow-clear />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="价格" v-bind="validateInfos.price" id="FcFishGroundForm-price" name="price">
								<a-input-number v-model:value="formData.price" placeholder="请输入价格" style="width: 100%" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="VIP 价格" v-bind="validateInfos.vipPrice" id="FcFishGroundForm-vipPrice" name="vipPrice">
								<a-input-number v-model:value="formData.vipPrice" placeholder="请输入VIP 价格" style="width: 100%" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="价格说明" v-bind="validateInfos.priceDesc" id="FcFishGroundForm-priceDesc" name="priceDesc">
								<a-textarea v-model:value="formData.priceDesc" :rows="4" placeholder="请输入价格说明" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="场地设施" v-bind="validateInfos.facilities" id="FcFishGroundForm-facilities" name="facilities">
								<j-checkbox type="checkbox" v-model:value="formData.facilities" dictCode="facilities" placeholder="请选择场地设施"  allow-clear />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="船只总数" v-bind="validateInfos.boatQuantity" id="FcFishGroundForm-boatQuantity" name="boatQuantity">
								<a-input-number v-model:value="formData.boatQuantity" placeholder="请输入船只总数" style="width: 100%" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="钓场状态" v-bind="validateInfos.status" id="FcFishGroundForm-status" name="status">
								<j-dict-select-tag v-model:value="formData.status" dictCode="fish_ground_status" placeholder="请选择钓场状态"  allow-clear />
							</a-form-item>
						</a-col>
          </a-row>
        </a-form>
      </template>
    </JFormContainer>

		<!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated style="overflow:hidden;">
      <a-tab-pane tab="钓场船只" key="fcFishBoat" :forceRender="true">
        <j-vxe-table
          :keep-source="true"
          resizable
          ref="fcFishBoatTableRef"
          :loading="fcFishBoatTable.loading"
          :columns="fcFishBoatTable.columns"
          :dataSource="fcFishBoatTable.dataSource"
          :height="340"
          :disabled="disabled"
          :rowNumber="true"
          :rowSelection="true"
          :toolbar="true"/>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script lang="ts">
  import { defineComponent, ref, reactive, computed, toRaw, onMounted } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { useValidateAntFormAndTable } from '/@/hooks/system/useJvxeMethods';
  import { queryFcFishBoatListByMainId, queryDataById, saveOrUpdate } from '../FcFishGround.api';
  import { JVxeTable } from '/@/components/jeecg/JVxeTable';
  import {fcFishBoatColumns} from '../FcFishGround.data';
  import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
  import { TimePicker } from 'ant-design-vue';
  import JImageUpload from '/@/components/Form/src/jeecg/components/JImageUpload.vue';
  import JCheckbox from "/@/components/Form/src/jeecg/components/JCheckbox.vue";
  import JFormContainer from '/@/components/Form/src/container/JFormContainer.vue';
  import { Form } from 'ant-design-vue';
  const useForm = Form.useForm;

  export default defineComponent({
    name: "FcFishGroundForm",
    components:{
      JDictSelectTag,
      TimePicker,
      JImageUpload,
      JCheckbox,
      JVxeTable,
			JFormContainer,
    },
    props:{
      formDisabled:{
        type: Boolean,
        default: false
      },
      formData: { type: Object, default: ()=>{} },
      formBpm: { type: Boolean, default: true }
    },
    emits:['success'],
    setup(props, {emit}) {
      const loading = ref(false);
      const formRef = ref();
      const fcFishBoatTableRef = ref();
      const fcFishBoatTable = reactive<Record<string, any>>({
        loading: false,
        columns: fcFishBoatColumns,
        dataSource: []
      });
      const activeKey = ref('fcFishBoat');
      const formData = reactive<Record<string, any>>({
        id: '',
        name: '',   
        homeImage: '',   
        detailsImage: '',   
        introduce: '',   
        address: '',   
        phone: '',   
        positionQuantity: undefined,
        startTime: '',   
        endTime: '',   
        price: undefined,
        vipPrice: undefined,
        priceDesc: '',   
        facilities: '',   
        boatQuantity: undefined,
        status: '1',   
      });

      //表单验证
      const validatorRules = reactive({
        name: [{ required: true, message: '请输入钓场名称!'},],
        homeImage: [{ required: true, message: '请输入钓场首页图!'},],
        detailsImage: [{ required: true, message: '请输入钓场详情图!'},],
        introduce: [{ required: true, message: '请输入钓场介绍!'},],
        address: [{ required: true, message: '请输入钓场地址!'},],
        phone: [{ required: true, message: '请输入联系电话!'},],
        positionQuantity: [{ required: true, message: '请输入钓位数量!'},],
        startTime: [{ required: true, message: '请输入营业开始时间!'},],
        endTime: [{ required: true, message: '请输入营业结束时间!'},],
        price: [{ required: true, message: '请输入价格!'},],
        vipPrice: [{ required: true, message: '请输入VIP 价格!'},],
        boatQuantity: [{ required: true, message: '请输入船只总数!'},],
        status: [{ required: true, message: '请输入钓场状态!'},],
      });
      const {resetFields, validate, validateInfos} = useForm(formData, validatorRules, {immediate: false});
      const dbData = {};
      const formItemLayout = {
        labelCol: {xs: {span: 24}, sm: {span: 5}},
        wrapperCol: {xs: {span: 24}, sm: {span: 16}},
      };

      // 表单禁用
      const disabled = computed(()=>{
        if(props.formBpm === true){
          if(props.formData.disabled === false){
            return false;
          }else{
            return true;
          }
        }
        return props.formDisabled;
      });

      

      function add() {
        resetFields();
        fcFishBoatTable.dataSource = [];
      }

      async function edit(row) {
        //主表数据
        await queryMainData(row.id);
        //子表数据
        const fcFishBoatDataList = await queryFcFishBoatListByMainId(row['id']);
        fcFishBoatTable.dataSource = [...fcFishBoatDataList];
      }

      async function queryMainData(id) {
        const row = await queryDataById(id);
        resetFields();
        const tmpData = {};
        Object.keys(formData).forEach((key) => {
          if(row.hasOwnProperty(key)){
            tmpData[key] = row[key]
          }
        })
        //赋值
        Object.assign(formData,tmpData);
      }

      const {getSubFormAndTableData, transformData} = useValidateAntFormAndTable(activeKey, {
        'fcFishBoat': fcFishBoatTableRef,
      });

      async function getFormData() {
        try {
          // 触发表单验证
          await validate();
        } catch ({ errorFields }) {
          if (errorFields) {
            const firstField = errorFields[0];
            if (firstField) {
              formRef.value.scrollToField(firstField.name, { behavior: 'smooth', block: 'center' });
            }
          }
          return Promise.reject(errorFields);
        }
        return transformData(toRaw(formData))
      }

      async function submitForm() {
        const mainData = await getFormData();
        const subData = await getSubFormAndTableData();
        const values = Object.assign({}, dbData, mainData, subData);
        console.log('表单提交数据', values)
        const isUpdate = values.id ? true : false
        await saveOrUpdate(values, isUpdate);
        //关闭弹窗
        emit('success');
      }
      
      function setFieldsValue(values) {
        if(values){
          Object.keys(values).map(k=>{
            formData[k] = values[k];
          });
        }
      }

      /**
       * 值改变事件触发-树控件回调
       * @param key
       * @param value
       */
      function handleFormChange(key, value) {
        formData[key] = value;
      }


      return {
        fcFishBoatTableRef,
        fcFishBoatTable,
        validatorRules,
        validateInfos,
        activeKey,
        loading,
        formData,
        setFieldsValue,
        handleFormChange,
        formItemLayout,
        disabled,
        getFormData,
        submitForm,
        add,
        edit,
        formRef,
      }
    }
  });
</script>
<style lang="less" scoped>
  .ant-tabs-tabpane.sub-one-form {
    max-height: 340px;
    overflow: auto;
  }
</style>
