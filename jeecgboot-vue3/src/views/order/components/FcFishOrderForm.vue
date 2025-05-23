<template>
  <a-spin :spinning="confirmLoading">
    <JFormContainer :disabled="disabled">
      <template #detail>
        <a-form ref="formRef" class="antd-modal-form" :labelCol="labelCol" :wrapperCol="wrapperCol" name="FcFishOrderForm">
          <a-row>
						<a-col :span="24">
							<a-form-item label="小程序用户ID" v-bind="validateInfos.username" id="FcFishOrderForm-username" name="username">
								<a-input v-model:value="formData.username" placeholder="请输入小程序用户ID" disabled allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="小程序用户昵称" v-bind="validateInfos.realname" id="FcFishOrderForm-realname" name="realname">
								<j-popup
									placeholder="请选择小程序用户昵称"
									v-model:value="formData.realname"
									code="fc_fish_user_report"
									:fieldConfig="[
										{ source: 'id', target: 'userId' },
										{ source: 'username', target: 'username' },
										{ source: 'realname', target: 'realname' },
									]"
									:multi="false"
									:setFieldsValue="setFieldsValue"
									 allow-clear />							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="钓场名称" v-bind="validateInfos.groundName" id="FcFishOrderForm-groundName" name="groundName">
								<a-input v-model:value="formData.groundName" placeholder="请输入钓场名称" disabled allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="预约日期" v-bind="validateInfos.date" id="FcFishOrderForm-date" name="date">
								<a-date-picker placeholder="请选择预约日期"  v-model:value="formData.date" value-format="YYYY-MM-DD"  style="width: 100%"  allow-clear />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="船号" v-bind="validateInfos.boatNumber" id="FcFishOrderForm-boatNumber" name="boatNumber">
								<j-popup
									placeholder="请选择船号"
									v-model:value="formData.boatNumber"
									code="fc_fish_boat_report"
									:fieldConfig="[
										{ source: 'id', target: 'groundId' },
										{ source: 'name', target: 'groundName' },
										{ source: 'boat_id', target: 'boatId' },
										{ source: 'boat_number', target: 'boatNumber' },
									]"
									:multi="false"
									:setFieldsValue="setFieldsValue"
									 allow-clear />							</a-form-item>
						</a-col>
            <a-col :span="24">
							<a-form-item label="预约用户姓名" v-bind="validateInfos.name" id="FcFishOrderForm-name" name="name">
								<a-input v-model:value="formData.name" placeholder="请输入预约用户姓名"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="预约用户手机号" v-bind="validateInfos.phone" id="FcFishOrderForm-phone" name="phone">
								<a-input v-model:value="formData.phone" placeholder="请输入预约用户手机号"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="预约票价" v-bind="validateInfos.fare" id="FcFishOrderForm-fare" name="fare">
								<a-input-number v-model:value="formData.fare" placeholder="请输入预约票价" style="width: 100%" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="预约状态" v-bind="validateInfos.status" id="FcFishOrderForm-status" name="status">
								<j-dict-select-tag v-model:value="formData.status" dictCode="fish_order_status" placeholder="请选择预约状态"  allow-clear />
							</a-form-item>
						</a-col>
            <a-col :span="24">
							<a-form-item label="创建日期" v-bind="validateInfos.createTime" id="FcFishOrderForm-createTime" name="createTime">
								<a-date-picker placeholder="请选择创建日期"  v-model:value="formData.createTime" showTime value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" disabled allow-clear />
							</a-form-item>
						</a-col>
          </a-row>
        </a-form>
      </template>
    </JFormContainer>
  </a-spin>
</template>

<script lang="ts" setup>
  import { ref, reactive, defineExpose, nextTick, defineProps, computed, onMounted } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { useMessage } from '/@/hooks/web/useMessage';
  import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
  import JPopup from '/@/components/Form/src/jeecg/components/JPopup.vue';
  import { getValueType } from '/@/utils';
  import { saveOrUpdate } from '../FcFishOrder.api';
  import { Form } from 'ant-design-vue';
  import JFormContainer from '/@/components/Form/src/container/JFormContainer.vue';
  const props = defineProps({
    formDisabled: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({})},
    formBpm: { type: Boolean, default: true }
  });
  const formRef = ref();
  const useForm = Form.useForm;
  const emit = defineEmits(['register', 'ok']);
  const formData = reactive<Record<string, any>>({
    id: '',
    username: '',   
    realname: '',   
    groundName: '',   
    date: '',   
    boatNumber: '',   
    name: '',   
    phone: '',   
    fare: undefined,
    status: '',   
    createTime: '',   
  });
  const { createMessage } = useMessage();
  const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
  const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
  const confirmLoading = ref<boolean>(false);
  // 表单验证
  const validatorRules = reactive({
    username: [{ required: true, message: '请输入小程序用户ID!'},],
    realname: [{ required: true, message: '请输入小程序用户昵称!'},],
    groundName: [{ required: true, message: '请输入钓场名称!'},],
    date: [{ required: true, message: '请输入预约日期!'},],
    boatNumber: [{ required: true, message: '请输入船号!'},],
    name: [{ required: true, message: '请输入预约用户姓名!'},],
    phone: [{ required: true, message: '请输入预约用户手机号!'},],
    fare: [{ required: true, message: '请输入预约票价!'},],
    status: [{ required: true, message: '请输入预约状态!'},],
  });
  const { resetFields, validate, validateInfos } = useForm(formData, validatorRules, { immediate: false });

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

  
  /**
   * 新增
   */
  function add() {
    edit({});
  }

  /**
   * 编辑
   */
  function edit(record) {
    nextTick(() => {
      resetFields();
      const tmpData = {};
      Object.keys(formData).forEach((key) => {
        if(record.hasOwnProperty(key)){
          tmpData[key] = record[key]
        }
      })
      //赋值
      Object.assign(formData, tmpData);
    });
  }

  /**
   * 提交数据
   */
  async function submitForm() {
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
    confirmLoading.value = true;
    const isUpdate = ref<boolean>(false);
    //时间格式化
    let model = formData;
    if (model.id) {
      isUpdate.value = true;
    }
    //循环数据
    for (let data in model) {
      //如果该数据是数组并且是字符串类型
      if (model[data] instanceof Array) {
        let valueType = getValueType(formRef.value.getProps, data);
        //如果是字符串类型的需要变成以逗号分割的字符串
        if (valueType === 'string') {
          model[data] = model[data].join(',');
        }
      }
    }
    await saveOrUpdate(model, isUpdate.value)
      .then((res) => {
        if (res.success) {
          createMessage.success(res.message);
          emit('ok');
        } else {
          createMessage.warning(res.message);
        }
      })
      .finally(() => {
        confirmLoading.value = false;
      });
  }

  /**
   *  popup组件值改变事件
   */
  function setFieldsValue(map) {
    Object.keys(map).map((key) => {
      formData[key] = map[key];
    });
  }

  defineExpose({
    add,
    edit,
    submitForm,
  });
</script>

<style lang="less" scoped>
  .antd-modal-form {
    padding: 14px;
  }
</style>
