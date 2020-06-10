<template>
  <a-drawer
    title="查看"
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
        label="药品"
      >
        <up-fc ref="upfc">
        </up-fc>
      </a-form-item>
     <a-form-item
        v-bind="formItemLayout"
        label="配送开始日期"
      >
         <a-date-picker
              v-decorator="[
          'sendStartTime',
          { rules: [{ required:true , message: '请输入配送开始日期' }] },
        ]"
              placeholder="请输入配送开始日期"
              @change="onChange"
            />
      </a-form-item>
       <a-form-item
        v-bind="formItemLayout"
        label="配送结束日期"
      >
         <a-date-picker
              v-decorator="[
          'sendEndTime',
          { rules: [{ required:true , message: '请输入配送结束日期' }] },
        ]"
              placeholder="请输入配送结束日期"
              @change="onChange"
            />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="配送委托函上传"
      >
        <a-upload
          :fileList="fileList"
          :remove="handleRemove"
          :beforeUpload="beforeUpload"
        >
        <!--  <a-button>
       <a-icon type="upload" /> 选择文件 </a-button> -->
        </a-upload>
       <!--  <a-button
          type="primary"
          @click="handleUpload"
          :disabled="fileList.length === 0 ||isShow===0"
          :loading="uploading"
          style="margin-top: 16px"
        >
          {{uploading ? '上传中' : '开始上传' }}
        </a-button>-->
      </a-form-item>
    </a-form>
    <div class="drawer-bootom-button">
        <a-button @click="onClose" style="margin-right: .8rem">关闭</a-button>
    </div>
  </a-drawer>
</template>
<script>
import upFc from '../../common/UpFileCustomer'
import moment from 'moment'

const formItemLayout = {
  labelCol: { span: 5 },
  wrapperCol: { span: 16 }
}
export default {
  name: 'ScmBGysMatersendEdit',
  components: { upFc },
  props: {
    editVisiable: {
      default: false
    },
    isShowsub: {
      default: true
    }
  },
  data () {
    return {
      isShow: 1,
      fileList: [],
      uploading: false,
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      scmBGysmatersend: {
        fileId: '',
        materId: ''
      }
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.$refs.upfc.reset()
      this.$refs.upfc.matnr = ''
      this.fileList = []
      this.scmBGysmatersend.materId = ''
      this.scmBGysmatersend.fileId = ''
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    setFormValues ({ ...scmBGysmatersend }) {
      let fields = ['sendStartTime', 'sendEndTime']
      let fieldDates = ['sendStartTime', 'sendEndTime']
      Object.keys(scmBGysmatersend).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (scmBGysmatersend[key] !== '') {
              obj[key] = moment(scmBGysmatersend[key])
            }
          } else {
            obj[key] = scmBGysmatersend[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.scmBGysmatersend.id = scmBGysmatersend.id
      this.scmBGysmatersend.materId = scmBGysmatersend.materId
      this.scmBGysmatersend.matnr = scmBGysmatersend.matnr
      //console.info(this.$refs.upfc)
      if (scmBGysmatersend.fileId) {
        if (scmBGysmatersend.fileId != '') {
          this.scmBGysmatersend.fileId = scmBGysmatersend.fileId
          this.isShow = 0
          this.fileList = []
          this.$get('comFile/' + scmBGysmatersend.fileId).then((r) => {
            this.$refs.upfc.setFormValue(scmBGysmatersend.matnr + '_' + scmBGysmatersend.txz01,scmBGysmatersend.matnr, scmBGysmatersend.materId)
            let data = r.data
            this.fileList.push({
              uid: data.id,
              name: data.clientName,
              status: 'done',
              url: this.$baseUrl + 'uploadFile/' + data.serverName
            })
          })
        }
      }
    },
    handleRemove (file) {
      const index = this.fileList.indexOf(file)
      const newFileList = this.fileList.slice()
      newFileList.splice(index, 1)
      this.fileList = newFileList
      this.isShow = 1
    },
    onChange (date, dateString) {
      console.log(date, dateString);
    },
    beforeUpload (file) {
      const isJPG = file.type === 'pdf'
      if (!isJPG) {
        this.$message.error('请只上传pdf文件!')
      }
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('附件必须小于 2MB!')
      }
      if (isJPG && isLt2M) {
        this.fileList = [...this.fileList, file]
      }
      return isJPG && isLt2M
    },
    handleUpload () {
      const { fileList } = this
      const formData = new FormData()
      formData.append('file', fileList[0])
      this.uploading = true

      // You can use any AJAX library you like
      this.$upload('comFile/upload', formData).then((r) => {
        this.scmBGysmatersend.fileId = r.data.data
        //this.fileList = []
        this.isShow = 0
        this.uploading = false
        this.$message.success('上传成功.')
      }).catch(() => {
        this.uploading = false
        this.$message.error('上传失败.')
      })
    },
    handleSubmit () {
      if (this.scmBGysmatersend.materId == '') {
        this.$message.warning('请在下拉列表里选择药品.')
        return false
      }
      if (this.scmBGysmatersend.fileId == '') {
        this.$message.warning('请上传资质附件.')
        return false
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          let scmBGysmatersend = this.form.getFieldsValue()
          scmBGysmatersend.id = this.scmBGysmatersend.id
          scmBGysmatersend.materId = this.$refs.upfc.materId
          scmBGysmatersend.matnr = this.$refs.upfc.matnr
          scmBGysmatersend.fileId = this.scmBGysmatersend.fileId
          this.$put('scmBGysmatersend', {
            ...scmBGysmatersend
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
