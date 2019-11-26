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
      <a-row>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="供应数量"
          >
            <a-input-number
              :min="0"
              :max="amount"
              @blur="mengeBlur"
              placeholder="请输入供应数量"
              v-decorator="['gMenge',  { rules: [{ required: true, message: '供应数量不能为空' }] }]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="联系人"
          >
            <a-input
              placeholder="请输入联系人"
              v-decorator="['linkPerson', { rules: [{ required: true, message: '联系人不能为空' }] }]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="送达科室"
          >
           <a-input
              placeholder="请输入送达科室"
              v-decorator="['sendDepart', { rules: [{ required: true, message: '送达科室不能为空' }] }]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="联系方式"
          >
            <a-input
              placeholder="请输入联系方式"
              v-decorator="['linkTelephone', { rules: [{ required: true, message: '联系方式不能为空' }] }]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="发票号码"
          >
            <a-input
              placeholder="请输入发票号码"
              v-decorator="['fphm', { rules: [{ required: true, message: '发票号码不能为空' }] }]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="发票金额"
          >
            <a-input
              :disabled="true"
              placeholder="请输入发票金额"
              v-decorator="['fpjr', { rules: [{ required: true, message: '发票金额不能为空' }] }]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="开票日期"
          >
            <a-date-picker v-decorator="[ 'fprq', { rules: [{ required: true, message: '开票日期不能为空' }] }]" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="商品条码"
          >
            <a-input
              placeholder="请输入商品条码"
              v-decorator="['fpbm', {}]"
            />
          </a-form-item>
        </a-col>
      </a-row>
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
  labelCol: { span: 5 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'ScmBSupplyplanAdd',
  props: {
    addVisiable: {
      default: false
    },
    baseId: '',
    price: {
      type: Number,
      default: 0
    },
    amount: {
      type: Number,
      default: 0
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      scmBSupplyplan: {
      },
      chargeData: []
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
    mengeBlur (e) {
      if (e.target.value) {
        let money = this.price * e.target.value
        this.form.setFields({ fpjr: { value: money } })
      }

    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          this.scmBSupplyplan.baseId=this.baseId
          this.scmBSupplyplan.status=0
          this.scmBSupplyplan.bsartD=1//订单类型 物资
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
      let values = this.form.getFieldsValue(['gMenge', 'linkPerson', 'fphm', 'fpjr', 'fprq', 'sendDepart', 'linkTelephone', 'materCode'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.scmBSupplyplan[_key] = values[_key] })
      }
    }
  },
  watch: {
    addVisiable: {
      handler: function () {
        // if (this.addVisiable) {
        //   this.$get("scmBGysMaterPic/charge/" + this.baseId).then(r => {
        //     this.chargeData = r.data
        //   })
        // }
      }
    }
  }
}
</script>
