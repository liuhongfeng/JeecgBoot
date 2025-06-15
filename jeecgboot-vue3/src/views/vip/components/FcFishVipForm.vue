<template>
  <a-spin :spinning="confirmLoading">
    <JFormContainer :disabled="disabled">
      <template #detail>
        <a-form ref="formRef" class="antd-modal-form" :labelCol="labelCol" :wrapperCol="wrapperCol" name="FcFishVipForm">
          <a-row>
						<a-col :span="24">
							<a-form-item label="小程序用户ID" v-bind="validateInfos.username" id="FcFishVipForm-username" name="username">
								<a-input v-model:value="formData.username" placeholder="请输入小程序用户ID" disabled allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="小程序用户昵称" v-bind="validateInfos.realname" id="FcFishVipForm-realname" name="realname">
								<j-popup
									placeholder="请选择小程序用户昵称"
									v-model:value="formData.realname"
									code="fc_fish_no_vip_user_report"
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
							<a-form-item label="会员姓名" v-bind="validateInfos.name" id="FcFishVipForm-name" name="name">
								<a-input v-model:value="formData.name" placeholder="请输入会员姓名"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="会员手机号" v-bind="validateInfos.phone" id="FcFishVipForm-phone" name="phone">
								<a-input v-model:value="formData.phone" placeholder="请输入会员手机号"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
            <a-col :span="24">
							<a-form-item label="会员次数" v-bind="validateInfos.count" id="FcFishVipForm-count" name="count">
								<a-input-number v-model:value="formData.count" placeholder="请输入会员次数" style="width: 100%" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="创建日期" v-bind="validateInfos.createTime" id="FcFishVipForm-createTime" name="createTime">
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
  import JPopup from '/@/components/Form/src/jeecg/components/JPopup.vue';
  import { getValueType } from '/@/utils';
  import { saveOrUpdate } from '../FcFishVip.api';
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
    name: '',   
    phone: '',   
    count: undefined,
    createTime: '',   
  });
  const { createMessage } = useMessage();
  const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
  const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
  const confirmLoading = ref<boolean>(false);
  //表单验证
  const validatorRules = reactive({
    username: [{ required: true, message: '请输入小程序用户ID!'},],
    realname: [{ required: true, message: '请输入小程序用户昵称!'},],
    name: [{ required: true, message: '请输入会员姓名!'},],
    phone: [{ required: true, message: '请输入会员手机号!'}, { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},],
    count: [{ required: true}, { pattern: /^-?\d+$/, message: '请输入整数!'},],
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
