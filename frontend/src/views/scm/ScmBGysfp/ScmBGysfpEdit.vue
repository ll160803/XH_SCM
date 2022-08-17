<template>
  <a-drawer
    title="查看"
    :maskClosable="false"
     width="80%"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
     <a-form :form="form">
   <a-row>
    <a-col :sm="8">
 
      <a-form-item
        v-bind="formItemLayout"
        label="发票号码"
      >
        <a-input
          placeholder="请输入供应商发票号码"
          v-decorator="['fpHm', {rules: [{ required: true, message: '发票号码不能为空' }] }]"
        />
      </a-form-item>
      </a-col>
       <a-col :sm="8">
      <a-form-item
            v-bind="formItemLayout"
            label="发票日期"
          >
            <a-date-picker v-decorator="[ 'fprq', { rules: [{ required: true, message: '发票日期不能为空' }] }]" />
        </a-form-item>
         </a-col>
       <a-col :sm="8">
      <a-form-item
        v-bind="formItemLayout"
        label="发票金额"
      >
        <a-input-number
          :precision="2"
          placeholder="请输入供应商发票金额"
          v-decorator="['fpAmount', {rules: [{ required: true, message: '发票金额不能为空' }] }]"
        />
      </a-form-item>
       </a-col>
       <a-col :sm="8">
      <a-form-item
        v-bind="formItemLayout"
        label="药厂发票"
      >
        <a-upload
          accept=".jpg,.png,.pdf"
          :fileList="fileList"
          :remove="handleRemove"
          :beforeUpload="beforeUpload"
        >
         
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
       </a-col>
       <a-col :sm="8">
      <a-form-item
        v-bind="formItemLayout"
        label="供应商发票"
      >
        <a-upload
          accept=".jpg,.png,.pdf"
          :fileList="fileList2"
          :remove="handleRemove2"
          :beforeUpload="beforeUpload2"
        >
         
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
       </a-col>
   </a-row>
    </a-form>
    <div class="drawer-bootom-button">
      <a-button
        @click="onClose"
        type="primary"
      >关闭</a-button>
    </div>
     <!-- <a-collapse v-model="activeKey">
    <a-collapse-panel key="1" header="厂商发票">
       <product-fp ref="productFp" :isEdit="false">
       </product-fp>
    </a-collapse-panel>
    </a-collapse> -->
    <sub-view ref="sub">
    </sub-view>
  </a-drawer>
</template>
<script>
import moment from 'moment'
import SubView from './SubView.vue'
import ProductFp from './ProductFp.vue'

const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 18 }
}
export default {
  components: { SubView,ProductFp },
  name: 'ScmBGysfpEdit',
  props: {
    editVisiable: {
      default: false
    }
  },
   watch: {
    activeKey(key) {
      console.log(key);
    },
  },
  data () {
    return {
      isShow: 1,
      isShow2: 1,
      state: 0,
      fileList: [],
      fileList2: [],
      uploading: false,
      uploading2: false,
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      scmBGysfp: {
        fileId: '',
        materId: ''
      },
      activeKey: ['1']
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.fileList = []
      this.fileList2 = []
      this.scmBGysfp.materId = ''
      this.scmBGysfp.fileId = ''
      // this.$refs.productFp.reset()
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    setFormValues ({ ...scmBGysfp }) {
      let fields = ['fpHm', 'fpBm', 'fpAmount', 'fprq']
      let fieldDates = ['fprq']
      Object.keys(scmBGysfp).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (scmBGysfp[key] !== '') {
              obj[key] = moment(scmBGysfp[key])
            }
            else {
              obj[key] = ''
            }
          } else {
            obj[key] = scmBGysfp[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.scmBGysfp.id = scmBGysfp.id
      this.scmBGysfp.materId = scmBGysfp.materId
      this.state = scmBGysfp.state
      if (scmBGysfp.fileId) {
        if (scmBGysfp.fileId !== '') {
          this.scmBGysfp.fileId = scmBGysfp.fileId
          this.isShow = 0
          this.fileList = []
          this.$get('comFile/' + scmBGysfp.fileId).then((r) => {
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
      if (scmBGysfp.materId) {
        if (scmBGysfp.materId !== '') {
          this.scmBGysfp.materId = scmBGysfp.materId
          this.isShow2 = 0
          this.fileList2 = []
          this.$get('comFile/' + scmBGysfp.materId).then((r) => {
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
      let that= this 
      setTimeout(()=>{
        that.$refs.sub.search(scmBGysfp.fpHm,scmBGysfp.fpAmount)
      //  that.$refs.productFp.fetch(scmBGysfp.id)
      },300)
    },
    handleRemove (file) {
      this.scmBGysfp.fileId = ''
      this.fileList = []
      this.isShow = 1
    },
    handleRemove2 (file) {
      this.scmBGysfp.materId = ''
      this.fileList2 = []
      this.isShow2 = 1
    },
    onChange (date, dateString) {
      console.log(date, dateString)
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
      return isJPG && isLt2M
    },
    beforeUpload2 (file) {
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
        this.fileList2 = [...this.fileList2, file]
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
        this.scmBGysfp.fileId = r.data.data
        // this.fileList = []
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
        this.scmBGysfp.materId = r.data.data
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
      if (this.scmBGysfp.materId === '') {
        this.$message.warning('请上传供应商发票.')
        return false
      }
      if (this.scmBGysfp.fileId === '') {
        this.$message.warning('请上传厂家发票.')
        return false
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          let scmBGysfp = this.form.getFieldsValue()
          scmBGysfp.materId = this.scmBGysfp.materId
          scmBGysfp.fileId = this.scmBGysfp.fileId
          scmBGysfp.id = this.scmBGysfp.id
          this.$put('scmBGysfp', {
            ...scmBGysfp
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
