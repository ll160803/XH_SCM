<template>
  <a-form :form="form1">
    <div v-show="current == 0">
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
    </div>
    <upload-file
      ref="file1"
      title="企业法人营业执照"
      displayIndex="1"
      v-show="current == 1"
    >
    </upload-file>
    <upload-file
      ref="file2"
      title="中华人民共和国组织结构代码证"
      displayIndex="2"
      v-show="current == 2"
    >
    </upload-file>
    <upload-file
      ref="file3"
      title="税务登记证"
      displayIndex="3"
      v-show="current == 3"
    >
    </upload-file>
    <upload-file
      ref="file4"
      title="中华人民共和国药品经营许可证"
      displayIndex="4"
      v-show="current == 4"
    >
    </upload-file>
    <upload-file
      ref="file5"
      title="中华人民共和国药品经营许可证副本及变更记录"
      displayIndex="5"
      v-show="current == 5"
    >
    </upload-file>
    <upload-file
      ref="file6"
      title="中华人民共和国药品经营质量管理规范认证证书(GSP)"
      displayIndex="6"
      v-show="current == 6"
    >
    </upload-file>
    <upload-file
      ref="file7"
      title="中华人民共和国医疗器械经营企业许可证"
      displayIndex="7"
      v-show="current == 7"
    >
    </upload-file>
    <upload-file
      ref="file8"
      title="中华人民共和国危险化学品经营许可证"
      displayIndex="8"
      v-show="current == 8"
    >
    </upload-file>
    <upload-file
      ref="file9"
      title="食品流通许可证"
      displayIndex="9"
      v-show="current == 9"
    >
    </upload-file>
    <upload-file
      ref="file10"
      title="开发票资料及银行账户信息"
      displayIndex="10"
      v-show="current == 10"
    >
    </upload-file>
    <upload-file
      ref="file11"
      title="企业税票模板"
      displayIndex="11"
      v-show="current == 11"
    >
    </upload-file>
    <upload-file
      ref="file12"
      title="企业出库单模板"
      displayIndex="12"
      v-show="current == 12"
    >
    </upload-file>
    <upload-file
      ref="file13"
      title="企业样章备案"
      displayIndex="13"
      v-show="current == 13"
    >
    </upload-file>
    <upload-file
      ref="file14"
      title="药品销售单位首次开户应收集资料"
      displayIndex="14"
      v-show="current == 14"
    >
    </upload-file>
    <upload-file
      ref="file15"
      title="企业基本情况和质量保证体系情况表"
      displayIndex="15"
      v-show="current == 15"
    >
    </upload-file>
    <upload-file
      ref="file16"
      title="下游客户法人授权委托书模板"
      displayIndex="16"
      v-show="current == 16"
    >
    </upload-file>
    <upload-file
      ref="file17"
      title="药品供需双方质量保证协议(正本)"
      displayIndex="17"
      v-show="current == 17"
    >
    </upload-file>
    <upload-file
      ref="file18"
      title="药品供需双方质量保证协议(副本)"
      displayIndex="18"
      v-show="current == 18"
    >
    </upload-file>
    <a-button
      v-if="current < 18"
      type="primary"
      @click="next"
    >
      下一步
    </a-button>
    <a-button
      v-if="current>0"
      style="margin-left: 8px"
      @click="prev"
    >
      上一步
    </a-button>
  </a-form>
</template>

<script>
import UploadFile from './UploadFile'

const formItemLayout = {
  labelCol: { span: 5 },
  wrapperCol: { span: 18 },
};
export default {
  components: { UploadFile },
  data () {
    return {
      current: 0,
      checkNick: false,
      formItemLayout,
      form1: this.$form.createForm(this, { name: 'dynamic_rule' }),
      attachList: [
        { title: "企业法人营业执照", isRequire: true },
        { title: "中华人民共和国组织结构代码证", isRequire: true },
        { title: "税务登记证", isRequire: true },
        { title: "中华人民共和国药品经营许可证", isRequire: true },
        { title: "中华人民共和国药品经营许可证副本及变更记录", isRequire: true },
        { title: "中华人民共和国药品经营质量管理规范认证证书(GSP)", isRequire: true },
        { title: "中华人民共和国医疗器械经营企业许可证", isRequire: true },
        { title: "中华人民共和国危险化学品经营许可证", isRequire: true },
        { title: "食品流通许可证", isRequire: true },
        { title: "开发票资料及银行账户信息", isRequire: true },
        { title: "企业税票模板", isRequire: true },
        { title: "企业出库单模板", isRequire: true },
        { title: "企业样章备案", isRequire: true },
        { title: "药品销售单位首次开户应收集资料", isRequire: true },
        { title: "企业基本情况和质量保证体系情况表", isRequire: true },
        { title: "下游客户法人授权委托书模板", isRequire: true },
        { title: "药品供需双方质量保证协议(正本)", isRequire: true },
        { title: "药品供需双方质量保证协议(副本)", isRequire: true },
      ]
    };
  },
  methods: {
    check () {
      this.form1.validateFields(err => {
        if (!err) {
          console.info('success');
        }
      })
    },
    handleChange (e) {
      this.checkNick = e.target.checked;
      this.$nextTick(() => {
        this.form1.validateFields(['nickname'], { force: true })
      })
    },
    next () {
      this.current++
    },
    prev () {
      this.current--
    }
  }
};
</script>