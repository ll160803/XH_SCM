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
      <a-form-item
        v-bind="formItemLayout"
        label="药品"
      >
        <up-fc ref="upfc">
        </up-fc>
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="批次号"
      >
        <a-input
          placeholder="请输入批次号"
          v-decorator="['charge', {rules: [{ required: true, message: '批次号不能为空' }, {max: 20, message: '批次号最长20'}]}]"
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
        label="药检报告"
      >
        <a-upload
          :fileList="fileList"
          :remove="handleRemove"
          :beforeUpload="beforeUpload"
        >
          <a-button>
            <a-icon type="upload" /> 选择文件 </a-button>
        </a-upload>
        <a-button
          type="primary"
          @click="handleUpload"
          :disabled="fileList.length === 0 ||isShow===0"
          :loading="uploading"
          style="margin-top: 16px"
        >
          {{uploading ? '上传中' : '开始上传' }}
        </a-button>
      </a-form-item>
       <a-form-item
        v-bind="formItemLayout"
        label="厂商发票"
      >
        <a-upload
          accept=".png,.jpg,.pdf"
          :fileList="fileList2"
          :remove="handleRemove2"
          :beforeUpload="beforeUpload2"
        >
          <a-button>
            <a-icon type="upload" /> 选择文件 </a-button>
        </a-upload>
        <a-button
          type="primary"
          @click="handleUpload2"
          :disabled="fileList2.length === 0 ||isShow2===0"
          :loading="uploading2"
          style="margin-top: 16px"
        >
          {{uploading2 ? '上传中' : '开始上传' }}
        </a-button>
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
        v-show="isShowsub"
      >提交</a-button>
    </div>
  </a-drawer>
</template>
<script>
import upFc from '../../common/UpFileCustomer'

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'ScmBGysMaterPicEdit',
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
       isShow2: 1,
      fileList2: [],
      uploading2: false,
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      scmBGysMaterPic: {
        fileId: '',
        materId: '',
        mtart: ''
      }
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.$refs.upfc.reset()
      this.$refs.upfc.matnr = ''
      this.fileList = []
       this.fileList2 = []
      this.scmBGysMaterPic.materId = ''
      this.scmBGysMaterPic.fileId = ''
      this.scmBGysMaterPic.mtart = ''
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    setFormValues ({ ...scmBGysMaterPic }) {
      let fields = ['charge', 'comments']
      let fieldDates = ['createTime', 'modifyTime']
      Object.keys(scmBGysMaterPic).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (scmBGysMaterPic[key] !== '') {
              obj[key] = moment(scmBGysMaterPic[key])
            }
          } else {
            obj[key] = scmBGysMaterPic[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.scmBGysMaterPic.id = scmBGysMaterPic.id
      this.scmBGysMaterPic.materId = scmBGysMaterPic.materId
      this.scmBGysMaterPic.matnr = scmBGysMaterPic.matnr
      //console.info(this.$refs.upfc)
      if (scmBGysMaterPic.fileId) {
        if (scmBGysMaterPic.fileId != '') {
          this.scmBGysMaterPic.fileId = scmBGysMaterPic.fileId
          this.isShow = 0
          this.fileList = []
          this.$get('comFile/' + scmBGysMaterPic.fileId).then((r) => {
            this.$refs.upfc.setFormValue(scmBGysMaterPic.matnr + '_' + scmBGysMaterPic.txz01,scmBGysMaterPic.matnr, scmBGysMaterPic.materId)
            let data = r.data
            this.fileList.push({
              uid: data.id,
              name: data.clientName,
              status: 'done',
              url: this.$baseUrl + 'comFile/checkFile/' + data.serverName
            })
          })
        }
      }
       if (scmBGysMaterPic.mtart) {
        if (scmBGysMaterPic.mtart !== '') {
          this.scmBGysMaterPic.mtart = scmBGysMaterPic.mtart
          this.isShow2 = 0
          this.fileList2 = []
          this.$get('comFile/' + scmBGysMaterPic.mtart).then((r) => {
            let data = r.data
            this.fileList2.push({
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
     handleRemove2 (file) {
      const index = this.fileList2.indexOf(file)
      const newFileList = this.fileList2.slice()
      newFileList.splice(index, 1)
      this.fileList2 = newFileList
      this.isShow2 = 1
    },
    onChange (date, dateString) {
      console.log(date, dateString);
    },
    beforeUpload (file) {
      const isJPG = file.type === 'application/pdf'
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
    beforeUpload2 (file) {
      const isJPG = (file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'application/pdf' )
      console.info(file.type)
      if (!isJPG) {
        this.$message.error('请只上传jpg,png,pdf文件!')
      }
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('附件必须小于 2MB!')
      }
      if (isJPG && isLt2M) {
        this.fileList2 = [...this.fileList2, file]
      }
      return isJPG && isLt2M; 
    },
    handleUpload () {
      const { fileList } = this
      const formData = new FormData()
      formData.append('file', fileList[0])
      this.uploading = true

      // You can use any AJAX library you like
      this.$upload('comFile/uploadCheck', formData).then((r) => {
        this.scmBGysMaterPic.fileId = r.data.data
        //this.fileList = []
        this.isShow = 0
        this.uploading = false
        this.$message.success('上传成功.')
      }).catch(() => {
        this.uploading = false
        this.$message.error('上传失败.')
      })
      this.fileList[0].status = 'done'
    },
     handleUpload2 () {
      const { fileList2 } = this
      const formData = new FormData()
      formData.append('file', fileList2[0])
      this.uploading2 = true

      // You can use any AJAX library you like
      this.$upload('comFile/upload', formData).then((r) => {
        console.info('上传IF:' + r.data.data)
        this.scmBGysMaterPic.mtart = r.data.data

        // this.fileList = []
        this.isShow2 = 0
        this.uploading2 = false
        this.$message.success('上传成功.')
      }).catch(() => {
        this.uploading2 = false
        this.$message.error('上传失败.')
      })
      this.fileList2[0].status = 'done'
    },
    handleSubmit () {
      if (this.scmBGysMaterPic.materId == '') {
        this.$message.warning('请在下拉列表里选择药品.')
        return false
      }
      if (this.scmBGysMaterPic.fileId == '') {
        this.$message.warning('请上传资质附件.')
        return false
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          let scmBGysMaterPic = this.form.getFieldsValue()
          scmBGysMaterPic.id = this.scmBGysMaterPic.id
          scmBGysMaterPic.materId = this.$refs.upfc.materId
          scmBGysMaterPic.matnr = this.$refs.upfc.matnr
          scmBGysMaterPic.fileId = this.scmBGysMaterPic.fileId
          scmBGysMaterPic.mtart = this.scmBGysMaterPic.mtart
          this.$put('scmBGysMaterPic', {
            ...scmBGysMaterPic
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
