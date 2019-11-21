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
        label="主键"
      >
        <a-input
          placeholder="请输入主键"
          v-decorator="['id', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="编码"
      >
        <a-input
          placeholder="请输入编码"
          v-decorator="['code', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="姓名"
      >
        <a-input
          placeholder="请输入姓名"
          v-decorator="['name', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="供应商账号"
      >
        <a-input
          placeholder="请输入供应商账号"
          v-decorator="['gysaccount', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="药品编码"
      >
        <a-input
          placeholder="请输入药品编码"
          v-decorator="['materId', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="批次号"
      >
        <a-input
          placeholder="请输入批次号"
          v-decorator="['charge', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="备注"
      >
        <a-input
          placeholder="请输入备注"
          v-decorator="['comments', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="状态"
      >
        <a-input
          placeholder="请输入状态"
          v-decorator="['state', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="附件ID"
      >
        <a-input
          placeholder="请输入附件ID"
          v-decorator="['fileId', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="是否删除"
      >
        <a-input
          placeholder="请输入是否删除"
          v-decorator="['isDeletemark', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="创建时间"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'createTime', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="修改时间"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'modifyTime', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="创建人"
      >
        <a-input
          placeholder="请输入创建人"
          v-decorator="['createUserId', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="修改人"
      >
        <a-input
          placeholder="请输入修改人"
          v-decorator="['modifyUserId', {}]"
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
  name: 'ScmBGysMaterPicAdd',
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
      scmBGysMaterPic: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.scmBGysMaterPic = {}
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
          this.$post('scmBGysMaterPic', {
            ...this.scmBGysMaterPic
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
      let values = this.form.getFieldsValue(['id', 'code', 'name', 'gysaccount', 'materId', 'charge', 'comments', 'state', 'fileId', 'isDeletemark', 'createTime', 'modifyTime', 'createUserId', 'modifyUserId'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.scmBGysMaterPic[_key] = values[_key] })
      }
    }
  }
}
</script>
