<template>
  <a-card :title="title">
    <a-form :form="form">
      <a-form-item
        v-bind="formItemLayout"
        label="名称"
      >
        <a-input
          v-decorator="[
          'fileName',
          { rules: [{ required: this.isRequire, message: '请输入企业法人营业执照名称' }] },
        ]"
          placeholder="请输入企业法人营业执照名称"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="有效期起始"
      >
        <a-date-picker
          v-decorator="[
          'validdatestart',
          { rules: [{ required:false , message: '请输入有效期截至时间'}] },
        ]"
          placeholder="请输入有效期起始"
          @change="onChange"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="有效期"
      >
        <a-date-picker
          v-decorator="[
          'validdate',
          { rules: [{ required:this.isRequire , message: '请输入有效期截至时间' }] },
        ]"
          placeholder="请输入有效期截止时间"
          @change="onChange"
        />
      </a-form-item>
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
    </a-form>
  </a-card>
</template>
<script>
import moment from 'moment'

const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 15 },
};
export default {
  name: "file",
  data () {
    return {
      isShow: 1,
      uploading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      scmDVendorD: {
        title: this.title,
        fileName: this.fileName,
        validdatestart: this.validdatestart,
        validdate: this.validdate,
        fileIndex: this.displayIndex,
        fileId: this.fileId
      }
    }
  },
  props: {
    title: '',
    displayIndex: 0,
    isRequire: false,
    validdatestart: '',
    validdate: '',
    fileId: '',
    fileName: '',
    fileList: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    handleRemove (file) {
      const index = this.fileList.indexOf(file)
      const newFileList = this.fileList.slice()
      newFileList.splice(index, 1)
      this.fileList = newFileList
      this.scmDVendorD.fileId = ''//空 清空
      this.isShow = 1
    },
    onChange (date, dateString) {
      console.log(date, dateString);
    },
    beforeUpload (file) {
      this.fileList = [...this.fileList, file]
      return false
    },
    handleUpload () {
      const { fileList } = this
      const formData = new FormData()
      formData.append('file', fileList[0])
      this.uploading = true

      // You can use any AJAX library you like
      this.$upload('comFile/upload', formData).then((r) => {
        this.scmDVendorD.fileId = r.data.data
        //this.fileList = []
        this.isShow = 0
        this.uploading = false
        this.$message.success('上传成功.')
      }).catch(() => {
        this.uploading = false
        this.$message.error('上传失败.')
      })
    },
    setScmDAreaFields () {
      let values = this.form.getFieldsValue(['fileName', 'validdatestart', 'validdate'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.scmDVendorD[_key] = values[_key] })
      }
    },
    getScmDAreaFields () {
      let fields = ['fileName', 'validdatestart', 'validdate']
      let fieldDates = ['validdatestart', 'validdate']
      let entity = {
        fileName: this.fileName,
        validdatestart: this.validdatestart,
        validdate: this.validdate
      }
      Object.keys(entity).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (entity[key] !== '') {
              obj[key] = moment(entity[key])
            }
          } else {
          obj[key] = entity[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
    }
  },
  mounted () {
    if (this.fileList.length > 0) {
      this.isShow = 0
    }
     this.getScmDAreaFields()
  }
}
</script>
