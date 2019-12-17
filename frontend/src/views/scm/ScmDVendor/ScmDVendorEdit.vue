<template>
  <a-drawer
    title="详情"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-form-item
        v-bind="formItemLayout"
        label="名字"
      >
        <a-input
          placeholder="请输入名字"
          v-decorator="['name', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="地址"
      >
        <a-input
          placeholder="请输入地址"
          v-decorator="['address', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="联系人"
      >
        <a-input
          placeholder="请输入联系人"
          v-decorator="['linkPerson', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="联系电话"
      >
        <a-input
          placeholder="请输入联系电话"
          v-decorator="['phone', {}]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="邮件"
      >
        <a-input
          placeholder="请输入邮件"
          v-decorator="['email', {}]"
        />
      </a-form-item>
    </a-form>
    <attach-file
      v-for="item in attachList"
      :key="item.index"
      :ref="'file'+item.index"
      :title="item.title"
      :displayIndex="item.index"
      :isRequire="item.isRequire"
      :validdatestart="item.validdatestart"
      :validdate="item.validdate"
      :fileId="item.fileId"
      :fileName="item.fileName"
    >
    </attach-file>
    <div class="drawer-bootom-button">
      <a-button
        @click="onClose"
        type="primary"
      >关闭</a-button>
    </div>
  </a-drawer>
</template>
<script>
import moment from 'moment'
import AttachFile from './AttachFile'

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  components: { AttachFile },
  name: 'ScmDVendorEdit',
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
      scmDVendor: {},
      attachList: []
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
    setFormValues ({ ...scmDVendor }) {
      let fields = ['code', 'name', 'address', 'linkPerson', 'phone', 'email']
      let fieldDates = ['createTime', 'modifyTime']
      Object.keys(scmDVendor).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (scmDVendor[key] !== '') {
              obj[key] = moment(scmDVendor[key])
            }
          } else {
            obj[key] = scmDVendor[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.scmDVendor.id = scmDVendor.id
      this.$get('scmDVendorD/attach/' + scmDVendor.id, {
      }).then((r) => {
        let data = r.data.data
        if (data) {
          if (data.length > 0) {
            for (var index = 0; index < data.length; index++) {

              let entity = {
                fileName: '',
                validdatestart: '',
                validdate: ''
              }

              if (data[index].validDatestart != null) {
                entity.validdatestart = data[index].validDatestart
                this.attachList[index].validdatestart = data[index].validDatestart
              }
              if (data[index].validDate != null) {
                entity.validdate = data[index].validDate
                this.attachList[index].validdate = data[index].validDate
              }
              if (data[index].attachfile.id != null) {
                this.attachList[index].fileId = data[index].attachfile.id
              }
              if (data[index].filename != null) {
                entity.fileName = data[index].filename
              }

              this.$refs['file' + (index + 1)][0].getScmDAreaFields(entity)
              this.attachList[index].fileName = data[index].filename
              this.$refs['file' + (index + 1)][0].fileList = []

              if (data[index].attachfile.id !== '' && data[index].attachfile.id !== null) {
                this.$refs['file' + (index + 1)][0].isShow = 0
                this.$refs['file' + (index + 1)][0].fileList.push({
                  uid: data[index].attachfile.id,
                  name: data[index].attachfile.clientName,
                  status: 'done',
                  url: this.$baseUrl+'uploadFile/'+data[index].attachfile.serverName
                })
              }
            }
          }
        }
      })
    }
  }
}
</script>
