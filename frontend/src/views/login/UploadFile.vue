<template>
  <a-card
    :title="title"
  >
    <a-form-item
      v-bind="formItemLayout"
      label="名称"
    >
      <a-input
        v-model="scmDVendorD.fileName"
        v-decorator="[
          'fileName',
          { rules: [{ required: true, message: '请输入企业法人营业执照名称' }] },
        ]"
        placeholder="请输入企业法人营业执照名称"
      />
    </a-form-item>
    <a-form-item
      v-bind="formItemLayout"
      label="有效期起始"
    >
      <a-input
        v-model="scmDVendorD.valid_datestart"
        v-decorator="[
          'valid_datestart',
          { rules: [{  message: '请输入有效期起始' }] },
        ]"
        placeholder="请输入有效期起始"
      />
    </a-form-item>
    <a-form-item
      v-bind="formItemLayout"
      label="有效期"
    >
      <a-input
        v-model="scmDVendorD.valid_date"
        v-decorator="[
          'valid_date',
          { rules: [{ required: true, message: '请输入有效期截至时间' }] },
        ]"
        placeholder="请输入有效期截止时间"
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
      :disabled="fileList.length === 0"
      :loading="uploading"
      style="margin-top: 16px"
    >
      {{uploading ? '上传中' : '开始上传' }}
    </a-button>
  </a-card>
</template>
<script>
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 15 },
};
export default {
  data () {
    return {
      fileList: [],
      uploading: false,
      formItemLayout,
      scmDVendorD: {
        title: this.title,
        fileName: '',
        valid_datestart: '',
        valid_date: '',
        file_index: this.displayIndex,
        file_id: ''
      }
    }
  },
  props: {
    title:'',
    displayIndex: 0

  },
  methods: {
    handleRemove (file) {
      const index = this.fileList.indexOf(file)
      const newFileList = this.fileList.slice()
      newFileList.splice(index, 1)
      this.fileList = newFileList
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
        this.scmDVendorD.file_id = r
        this.fileList = []
        this.uploading = false
        this.$message.success('上传成功.')
      }).catch(() => {
        this.uploading = false
        this.$message.error('上传失败.')
      })
    }
  }
}
</script>
