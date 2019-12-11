<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
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
              style="width: 100%"
              placeholder="请输入供应数量"
              v-decorator="['gMenge',  { rules: [{ required: true, message: '供应数量不能为空' }] }]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="批号"
          >
            <a-select
              mode="single"
              :allowClear="true"
              style="width: 100%"
              v-decorator="[
          'charge',
          { rules: [{ required: true, message: '请选择批号' }] }
        ]"
            >
              <a-select-option
                v-for="r in chargeData"
                :key="r"
              >{{
          r
        }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="有效期"
          >
            <a-date-picker v-decorator="[ 'vfdat', { rules: [{ required: true, message: '有效期不能为空' }] }]" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="生产日期"
          >
            <a-date-picker v-decorator="[ 'hsdat', { rules: [{ required: true, message: '生产日期不能为空' }] }]" />
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
            label="发票日期"
          >
            <a-date-picker v-decorator="[ 'fprq', { rules: [{ required: true, message: '发票日期不能为空' }] }]" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="发票编码"
          >
            <a-input
              placeholder="请输入发票编码"
              v-decorator="['fpbm', {}]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="包装规格"
          >
            <a-input-number
              @blur="pkgNumberBlur"
              placeholder="请输入包装规格"
              v-decorator="['pkgAmount', { rules: [{ required: true, message: '包装规格不能为空' }] }]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="包装数量"
          >
            <a-input
              style="width: 100%"
              placeholder="请输入包装数量"
              v-decorator="['pkgNumber',{ rules: [{ required: true, message: '包装数量不能为空' }] }]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="缺货原因"
          >
            <a-input
              placeholder="请输入缺货原因"
              v-decorator="['outCause', {}]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            v-bind="formItemLayout"
            label="补货日期"
          >
            <a-date-picker v-decorator="[ 'outDate', {}]" />
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
import moment from 'moment'

const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15 }
}
export default {
  name: 'ScmBSupplyplanEdit',
  props: {
    editVisiable: {
      default: false
    },
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
        baseId: '',
        id: ''
      },
      chargeData: []
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    setFormValues ({ ...scmBSupplyplan }) {
      let fields = ['gMenge', 'charge', 'vfdat', 'hsdat', 'fphm', 'fpjr', 'fprq', 'fpbm', 'pkgAmount', 'pkgNumber', 'outCause', 'outDate']
      let fieldDates = ['vfdat', 'hsdat', 'fprq', 'outDate', 'createTime', 'modifyTime']
      Object.keys(scmBSupplyplan).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (scmBSupplyplan[key] !== null && scmBSupplyplan[key] !== '') {
              obj[key] = moment(scmBSupplyplan[key])
            }
            else {
              obj[key] = ''
            }
          } else {
            obj[key] = scmBSupplyplan[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.scmBSupplyplan.id = scmBSupplyplan.id
      this.scmBSupplyplan.baseId = scmBSupplyplan.baseId
    },
    mengeBlur (e) {
      if (e.target.value) {
        let money = this.price * e.target.value
        this.form.setFields({ fpjr: { value: money } })
        let pkg = this.form.getFieldValue('pkgAmount')
        if (pkg) {
          let num = e.target.value / pkg
          this.form.setFields({ pkgNumber: { value: num } })
        }
      }

    },
    pkgNumberBlur (e) {
      if (e.target.value) {
        let gMenge = this.form.getFieldValue('gMenge')
        if (gMenge) {
          let num = gMenge / e.target.value
          this.form.setFields({ pkgNumber: { value: num } })
        }
      }
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let scmBSupplyplan = this.form.getFieldsValue()
          if(scmBSupplyplan.outDate==''){
            scmBSupplyplan.outDate=null
          }
          scmBSupplyplan.id = this.scmBSupplyplan.id
          scmBSupplyplan.baseId = this.scmBSupplyplan.baseId
          this.$put('scmBSupplyplan', {
            ...scmBSupplyplan
          }).then(() => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  },
  watch: {
    editVisiable: {
      handler: function () {
        if (this.editVisiable) {
          this.$get("scmBGysMaterPic/charge/" + this.scmBSupplyplan.baseId).then(r => {
            this.chargeData = r.data
          })
        }
      }
    }
  },
  mounted ()
  {
    // amount
  }
}
</script>
