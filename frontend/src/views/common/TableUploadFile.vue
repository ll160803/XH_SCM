<template>
<a-row>
  <a-col :span="12">
    <a-upload
      accept=".png,.jpg,.pdf"
      :fileList="fileList"
      :remove="handleRemove"
      :beforeUpload="beforeUpload"
    >
      <a-button v-if="fileList.length === 0">
        <a-icon type="upload" ref="up"  /> 选择文件
      </a-button>
    </a-upload></a-col>
    <a-col :span="12">
    <a-button
          type="primary"
          @click="handleUpload"
          :disabled="fileList.length === 0 ||isShow===0"
          :loading="uploading"
        >
          {{uploading ? '上传中' : '开始上传' }}
        </a-button>
    </a-col>
</a-row>

</template>
  
  <script>
export default {
  name: "file",
  data () {
    return {
      tstyle: { "color": "#0785fd", "font-weight": "bold", "background-color": "#ececec" },
      fileList: [],
      isShow: 1,
      uploading: false,
      fileUrl: '',
      fileId: '',
      fileName: ''
    }
  },
  methods: {
    handleRemove (file) {
      const index = this.fileList.indexOf(file)
      const newFileList = this.fileList.slice()
      newFileList.splice(index, 1)
      this.fileList = newFileList
      this.fileId = ''// 空 清空
      this.fileUrl = ''
      this.isShow = 1
    },
    onChange (date, dateString) {
      console.log(date, dateString);
    },
   beforeUpload (file) {
      const isJPG = (file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'application/pdf')
      console.info(file.type)
      if (!isJPG) {
        this.$message.error('请只上传jpg,png,pdf文件!')
      }
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('附件必须小于 2MB!')
      }
      if (isJPG && isLt2M) {
        this.fileList = [...this.fileList, file]
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
      this.$upload('comFile/uploadNew', formData).then((r) => {
        let comfile = r.data.data
        this.fileId = comfile.uid
        this.fileUrl = comfile.url
        this.fileName = comfile.name
        this.fileList=[]
        this.fileList.push(comfile)
        this.$emit("setFileId", this.fileId, this.fileUrl, this.fileName)
        this.isShow = 0
        this.uploading = false
        this.$message.success('上传成功.')
      }).catch(() => {
        this.uploading = false
        this.$message.error('上传失败.')
      })
    },
    fetch (fileId) {
      this.fileId = fileId
      this.fileList=[]
      this.$get('comFile/scm/' + fileId).then((r) => {
        let data = r.data
        this.fileUrl = data.url
        this.fileList.push(data)
      })
    },
    cancelAudit () {
      console.log(this.fileId)
      this.$emit("setFileId", this.fileId, this.fileUrl, this.fileName)
      this.fileList = []
      this.fileId = ''
      this.fileUrl = ''
      this.fileName =''
    }

  }
}
  </script>
  
  <style>
</style>