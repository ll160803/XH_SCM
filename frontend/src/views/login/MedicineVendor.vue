<template>
  <a-form :form="form1">
    <a-form-item
      v-bind="formItemLayout"
      label="供应商名称"
    >
      <a-input
        v-decorator="[
          'name',
          { rules: [{ required: true, message: '请输入供应商名称' }] },
        ]"
        placeholder="请输入供应商名称"
      />
    </a-form-item>
    <a-form-item
      v-bind="formItemLayout"
      label="联系人"
    >
      <a-input
        v-decorator="[
          'link_person',
          { rules: [{ required: true, message: '请输入联系人' }] },
        ]"
        placeholder="请输入联系人"
      />
    </a-form-item>
    <a-form-item
      v-bind="formItemLayout"
      label="联系电话"
    >
      <a-input
        v-decorator="[
          'phone',
          { rules: [{ required: true, message: '请输入联系电话' }] },
        ]"
        placeholder="请输入联系电话"
      />
    </a-form-item>
    <a-form-item
      v-bind="formItemLayout"
      label="电子邮件"
    >
      <a-input
        v-decorator="[
          'email',
          { rules: [{ required: true, message: '请输入电子邮件' }] },
        ]"
        placeholder="请输入电子邮件"
      />
    </a-form-item>
    <a-form-item
      v-bind="formItemLayout"
      label="办公地址"
    >
      <a-input
        v-decorator="[
          'address',
          { rules: [{ required: true, message: '请输入办公地址' }] },
        ]"
        placeholder="请输入办公地址"
      />
    </a-form-item>
    <upload-file
      ref="file1"
      title="企业法人营业执照"
      displayIndex="1"
    >
    </upload-file>
  </a-form>
</template>

<script>
import UploadFile from './UploadFile'

const formItemLayout = {
  labelCol: { span: 5 },
  wrapperCol: { span: 18 },
};
const formTailLayout = {
  labelCol: { span: 4 },
  wrapperCol: { span: 8, offset: 4 },
};
export default {
  components: { UploadFile },
  data () {
    return {
      checkNick: false,
      formItemLayout,
      formTailLayout,
      form1: this.$form.createForm(this, { name: 'dynamic_rule' }),
    };
  },
  methods: {
    check () {
      this.form1.validateFields(err => {
        if (!err) {
          console.info('success');
        }
      });
    },
    handleChange (e) {
      this.checkNick = e.target.checked;
      this.$nextTick(() => {
        this.form1.validateFields(['nickname'], { force: true });
      });
    },
  },
};
</script>