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
        label="药品"
      >
        <up-fc
          ref="upfc"
          v-decorator="['matnr2', {rules: [{ required: true, message: '药品不能为空' }]}]"
        >
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
          accept=".pdf"
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
      >提交</a-button>
    </div>
  </a-drawer>
</template>
<script>
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
import upFc from '../../common/UpFileCustomer'
export default {
  name: 'ScmBGysMaterPicAdd',
  components: { upFc },
  props: {
    addVisiable: {
      default: false
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
      this.scmBGysMaterPic = {}
      this.fileList = []
      this.isShow = 1
        this.fileList2 = []
      this.isShow2 = 1
      this.$refs.upfc.reset()
      this.$refs.upfc.matnr = ''
      this.scmBGysMaterPic.materId = ''
      this.scmBGysMaterPic.fileId = ''
      this.scmBGysMaterPic.mtart = ''  //厂商发票
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.scmBGysMaterPic.materId = this.$refs.upfc.materId
      this.scmBGysMaterPic.matnr =this.$refs.upfc.matnr
      if (this.scmBGysMaterPic.materId == '') {
        this.$message.warning('请在下列列表里选择药品.')
        return false
      }
      //  if (this.scmBGysMaterPic.mtart === '') {
      //   this.$message.warning('请上传厂家发票.')
      //   return false
      // }
      if (this.scmBGysMaterPic.fileId == '') {
        this.$message.warning('请上传验收资质附件.')
        return false
      }
      this.form.setFieldsValue({ matnr2: '2' })
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
      let values = this.form.getFieldsValue(['charge', 'comments'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.scmBGysMaterPic[_key] = values[_key] })
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
      //const index = this.fileList2.indexOf(file)
     // const newFileList = this.fileList2.slice()
     // newFileList.splice(index, 1)
      this.fileList2 = []
      this.isShow2 = 1
      this.uploading2= false
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
      if( isJPG && isLt2M)
      {
        this.fileList = [...this.fileList, file]
      }
      return isJPG && isLt2M;
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
        console.info("上传IF:" + r.data.data);
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
    }
  }
}
</script>
