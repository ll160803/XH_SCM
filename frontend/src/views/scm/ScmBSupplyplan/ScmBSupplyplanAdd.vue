<template>
  <a-drawer
    title="新增"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="addVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-form-item
        v-bind="formItemLayout"
        label="供应数量"
      >
        <a-input
          placeholder="请输入供应数量"
          v-decorator="['gMenge', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="批号"
      >
        <a-input
          placeholder="请输入批号"
          v-decorator="['charge', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="有效期"
      >
        <a-date-picker v-decorator="[ 'vfdat', {}]" />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="生产日期"
      >
        <a-date-picker v-decorator="[ 'hsdat', {}]" />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="发票号码"
      >
        <a-input
          placeholder="请输入发票号码"
          v-decorator="['fphm', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="发票金额"
      >
        <a-input
          placeholder="请输入发票金额"
          v-decorator="['fpjr', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="发票日期"
      >
        <a-date-picker v-decorator="[ 'fprq', {}]" />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="发票编码"
      >
        <a-input
          placeholder="请输入发票编码"
          v-decorator="['fpbm', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="包装规格"
      >
        <a-input
          placeholder="请输入包装规格"
          v-decorator="['pkgAmount', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="包装数量"
      >
        <a-input
          placeholder="请输入包装数量"
          v-decorator="['pkgNumber', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="缺货原因"
      >
        <a-input
          placeholder="请输入缺货原因"
          v-decorator="['outCause', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="补货日期"
      >
        <a-date-picker v-decorator="[ 'outDate', {}]" />
      </a-form-item>
    </a-form>
    <div class="drawer-bootom-button">
      <a-popconfirm
        title="确定放弃编辑？"
        @confirm="onClose"
        okText="确定"
        cancelText="取消"
      >
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button
        @click="handleSubmit"
        type="primary"
        :loading="loading"
      >提交</a-button>
    </div>
  </a-drawer>
</template>
<script>
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'ScmBSupplyplanAdd',
  props: {
    addVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      scmBSupplyplan: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.scmBSupplyplan = {}
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          this.$post('scmBSupplyplan', {
            ...this.scmBSupplyplan
          }).then(() => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['gMenge', 'charge', 'vfdat', 'hsdat', 'fphm', 'fpjr', 'fprq', 'fpbm', 'pkgAmount', 'pkgNumber', 'outCause', 'outDate'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.scmBSupplyplan[_key] = values[_key] })
      }
    }
  }
}
</script>
