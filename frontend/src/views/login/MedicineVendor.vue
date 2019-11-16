<template>
  <div>
    <a-form
      :form="form1"
      v-show="current == 0"
    >
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
          'linkperson',
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
    </a-form>

    <upload-file
      v-for="item in attachList"
      :key="item.index"
      :ref="'file'+item.index"
      :title="item.title"
      :displayIndex="item.index"
      v-show="current == item.index"
      :isRequire="item.isRequire"
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
    <a-button
      type="primary"
      :loading="loading"
      @click.stop.prevent="handleSubmit"
    >
      保存
    </a-button>
  </div>
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
      loading: false,
      current: 0,
      checkNick: false,
      formItemLayout,
      scmDVendor: {
        name: '',
        linkperson: '',
        phone: '',
        email: '',
        address: '',
        lb: 0//药品
      },
      scmDVendorD: [],
      form1: this.$form.createForm(this, { name: 'dynamic_rule' }),
      attachList: [
        { title: "企业法人营业执照", isRequire: true, index: 1 },
        { title: "中华人民共和国组织结构代码证", isRequire: true, index: 2 },
        { title: "税务登记证", isRequire: true, index: 3 },
        { title: "中华人民共和国药品经营许可证", isRequire: true, index: 4 },
        { title: "中华人民共和国药品经营许可证副本及变更记录", isRequire: false, index: 5 },
        { title: "中华人民共和国药品经营质量管理规范认证证书(GSP)", isRequire: true, index: 6 },
        { title: "中华人民共和国医疗器械经营企业许可证", isRequire: false, index: 7 },
        { title: "中华人民共和国危险化学品经营许可证", isRequire: false, index: 8 },
        { title: "食品流通许可证", isRequire: false, index: 9 },
        { title: "开发票资料及银行账户信息", isRequire: true, index: 10 },
        { title: "企业税票模板", isRequire: true, index: 11 },
        { title: "企业出库单模板", isRequire: true, index: 12 },
        { title: "企业样章备案", isRequire: true, index: 13 },
        { title: "药品销售单位首次开户应收集资料", isRequire: false, index: 14 },
        { title: "企业基本情况和质量保证体系情况表", isRequire: true, index: 15 },
        { title: "下游客户法人授权委托书模板", isRequire: true, index: 16 },
        { title: "药品供需双方质量保证协议(正本)", isRequire: true, index: 17 },
        { title: "药品供需双方质量保证协议(副本)", isRequire: true, index: 18 },
      ]
    };
  },
  methods: {
    check () {

    },
    handleChange (e) {
      this.checkNick = e.target.checked;
      this.$nextTick(() => {
        this.form1.validateFields(['nickname'], { force: true })
      })
    },
    handleSubmit () {
      // this.form1.validateFields(err => {
      //   if (!err) {
      //     for (let i = 1; i < 19; i++) {
      //       this.$refs["file" + i][0].form.validateFields(error => {
      //         if (error) {
      //           return false
      //         }
      //       })
      //     }
      //   }
      //   else {
      //     return false
      //   }
      // })
      this.setscmDVendorFields()
      // console.info(this.scmDVendor.name)
      //循环把子组件的值获取到
      for (let i = 1; i < 19; i++) {
        this.$refs["file" + i][0].setScmDAreaFields()
        // if (this.$refs["file" + i][0].isRequire && this.$refs["file" + i][0].scmDVendorD.fileId == "") {
        //   this.$message.error('请上传' + this.attachList[i - 1].title + '附件');
        //   return false
        // }
        if (this.$refs["file" + i][0].scmDVendorD.fileName != "") {
          this.scmDVendorD.push(this.$refs["file" + i][0].scmDVendorD)
        }
      }
      this.loading = true
      this.$post('scmDVendor/regist', {
        ...this.scmDVendor,
        scmDVendorD:JSON.stringify(this.scmDVendorD)
      }).then(() => {
        this.loading = false
        this.$message.success('注册成功')
        this.returnLogin()
      }).catch(() => {
        this.loading = false
        this.$message.error('抱歉，注册账号失败')
      })
    },
    setscmDVendorFields () {
      let values = this.form1.getFieldsValue(['name', 'linkperson', 'phone', 'email', 'address'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.scmDVendor[_key] = values[_key] })
      }
    },
    next () {
      this.form1.validateFields(err => {
        if (!err) {
          if (this.current > 0) {
            this.$refs["file" + this.current][0].form.validateFields(error => {
              if (!error) {
                this.current++
              }
            })
          }
          else {
            this.current++
          }
        }
      })
    },
    prev () {
      this.current--
    }
  }
};
</script>