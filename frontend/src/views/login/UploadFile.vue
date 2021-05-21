<template>
  <a-card
    :title="title"
    :headStyle="tstyle"
  >
    <a-form :form="form">
      <a-row>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-form-item
            v-bind="formItemLayout"
            label="有效期起始"
          >
            <a-date-picker
              v-decorator="[
          'validdatestart',
          { rules: [{ required:this.isRequire , message: '请输入有效期截至时间'}] },
        ]"
              placeholder="请输入有效期起始"
              @change="onChange"
            />
          </a-form-item>
        </a-col>
        <a-col
          :md="8"
          :sm="24"
        >
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
        </a-col>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-upload
            accept=".png,.jpg,.pdf,.gif"
            :fileList="fileList"
            :remove="handleRemove"
            :beforeUpload="beforeUpload"
            @change="handleChange"
            :disabled="!(fileList.length === 0)"
          >
            <a-button>
              <a-icon type="upload" /> 选择文件 </a-button>
          </a-upload>
          <!-- <a-button
            type="primary"
            @click="handleUpload"
            :disabled="fileList.length === 0 ||isShow===0"
            :loading="uploading"
            style="margin-top: 16px"
          >
            {{uploading ? '上传中' : '开始上传' }}
          </a-button> -->
        </a-col>
      </a-row>
    </a-form>
  </a-card>
</template>
<script>
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15 }
};
export default {
  name: "file",
  data () {
    return {
      tstyle: { "color": "#0785fd", "font-weight": "bold", "background-color": "#ececec" },
      fileList: [],
      isShow: 1,
      uploading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      scmDVendorD: {
        title: this.title,
        fileName: '',
        validdatestart: '',
        validdate: '',
        fileIndex: this.displayIndex,
        fileId: ''
      }
    }
  },
  props: {
    title: '',
    displayIndex: 0,
    isRequire: false
  },
  methods: {
    handleRemove (file) {
      const index = this.fileList.indexOf(file)
      const newFileList = this.fileList.slice()
      newFileList.splice(index, 1)
      this.fileList = newFileList
      this.scmDVendorD.fileId = ''// 空 清空
      this.isShow = 1
    },
    onChange (date, dateString) {
      console.log(date, dateString);
    },
    beforeUpload (file) {
      this.fileList =[]
        const isJPG = (file.type === 'application/pdf' ||file.type ==='image/png' || file.type ==='image/gif' ||file.type ==='image/jpeg')
      console.info(file.type)
      if (!isJPG) {
        this.$message.error('请只上传pdf，png, gif文件!')
      }
      const isLt2M = file.size / 1024 / 1024 < 20
      if (!isLt2M) {
        this.$message.error('附件必须小于 20MB!')
      }
      if (isJPG && isLt2M) {
        this.fileList = [...this.fileList, file]
       // this.handleUpload()
      }
      return isJPG && isLt2M;
    },
    handleChange (info) {
       if (info.file.status === 'uploading') {
         this.handleUpload()
       } 
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
      let values = this.form.getFieldsValue(['validdatestart', 'validdate'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.scmDVendorD[_key] = values[_key] })
      }
    }
  }
}
</script>
