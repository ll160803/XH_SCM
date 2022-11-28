<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width="650"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 55px); overflow: auto; padding-bottom: 53px"
  >
    <a-form :form="form">
      <a-form-item v-bind="formItemLayout" label="1=草药代煎；2=核医学">
        <a-input
          placeholder="请输入1=草药代煎；2=核医学"
          v-decorator="[
            'ywlx',
            {
              rules: [
                { required: true, message: '1=草药代煎；2=核医学不能为空' },
              ],
            },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="202210业务所属月份">
        <a-input
          placeholder="请输入202210业务所属月份"
          v-decorator="[
            'ny',
            {
              rules: [
                { required: true, message: '202210业务所属月份不能为空' },
              ],
            },
          ]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="院区	2000(本院=2000;肿瘤=2100;西院=2200;金银湖=2300)"
      >
        <a-input
          placeholder="请输入院区	2000(本院=2000;肿瘤=2100;西院=2200;金银湖=2300)"
          v-decorator="[
            'fyId',
            {
              rules: [
                {
                  required: true,
                  message:
                    '院区	2000(本院=2000;肿瘤=2100;西院=2200;金银湖=2300)不能为空',
                },
              ],
            },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="供应商ID">
        <a-input
          placeholder="请输入供应商ID"
          v-decorator="[
            'gysId',
            { rules: [{ required: true, message: '供应商ID不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="供应商名称">
        <a-input
          placeholder="请输入供应商名称"
          v-decorator="[
            'gysName',
            { rules: [{ required: true, message: '供应商名称不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="药品编码">
        <a-input
          placeholder="请输入药品编码"
          v-decorator="[
            'ypBh',
            { rules: [{ required: true, message: '药品编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="产地代码">
        <a-input
          placeholder="请输入产地代码"
          v-decorator="[
            'ypCd',
            { rules: [{ required: true, message: '产地代码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="药品名称">
        <a-input
          placeholder="请输入药品名称"
          v-decorator="[
            'ypMc',
            { rules: [{ required: true, message: '药品名称不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="数量">
        <a-input
          placeholder="请输入数量"
          v-decorator="[
            'sl',
            { rules: [{ required: true, message: '数量不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="单位">
        <a-input
          placeholder="请输入单位"
          v-decorator="[
            'dw',
            { rules: [{ required: true, message: '单位不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="进货金额">
        <a-input
          placeholder="请输入进货金额"
          v-decorator="[
            'jhJe',
            { rules: [{ required: true, message: '进货金额不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="创建人姓名">
        <a-input
          placeholder="请输入创建人姓名"
          v-decorator="[
            'cjr',
            { rules: [{ required: true, message: '创建人姓名不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="创建人工号">
        <a-input
          placeholder="请输入创建人工号"
          v-decorator="[
            'cjrid',
            { rules: [{ required: true, message: '创建人工号不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="备注">
        <a-input
          placeholder="请输入备注"
          v-decorator="[
            'remark',
            { rules: [{ required: true, message: '备注不能为空' }] },
          ]"
        />
      </a-form-item>
    </a-form>
    <div class="drawer-bootom-button">
      <a-popconfirm
        title="确定放弃编辑？"
        @confirm="onClose"
        okText="确定"
        cancelText="取消"
      >
        <a-button style="margin-right: 0.8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading"
        >提交</a-button
      >
    </div>
  </a-drawer>
</template>
<script>
import moment from 'moment'

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'ScmWSaleEdit',
  props: {
    editVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      scmWSale: {}
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
    setFormValues ({ ...scmWSale }) {
      let fields = ['ywlx', 'ny', 'fyId', 'gysId', 'gysName', 'ypBh', 'ypCd', 'ypMc', 'sl', 'dw', 'jhJe', 'cjr', 'cjrid', 'remark']
      let fieldDates = []
      Object.keys(scmWSale).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (scmWSale[key] !== '') {
              obj[key] = moment(scmWSale[key])
            }
            else {
              obj[key] = ''
            }
          } else {
            obj[key] = scmWSale[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.scmWSale.id = scmWSale.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let scmWSale = this.form.getFieldsValue()
          scmWSale.id = this.scmWSale.id                                       
          this.$put('scmWSale', {
            ...scmWSale
          }).then(() => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>
