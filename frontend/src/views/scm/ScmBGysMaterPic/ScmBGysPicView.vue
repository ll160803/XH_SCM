<template>
  <a-modal
    v-model="show"
    :centered="true"
    :keyboard="false"
    :footer="null"
    :width="750"
    @cancel="handleCancleClick"
    title="资质文件"
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
          v-decorator="['charge', {rules: [{ required: true, message: '批次号不能为空' }]}]"
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
        label="文件上传"
      >
        <a-upload
          :fileList="fileList"
        >
          <a-button>
            <a-icon type="upload" /> 选择文件 </a-button>
        </a-upload>
        <a-button
          type="primary"
          :disabled="fileList.length === 0 ||isShow===0"
          :loading="uploading"
          style="margin-top: 16px"
        >
          {{uploading ? '上传中' : '开始上传' }}
        </a-button>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script>
import upFc from '../../common/UpFileCustomer'

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'ScmBGysMaterPicView',
  components: { upFc },
  props: {
    editVisiable: {
      require: true,
      default: false
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
      scmBGysMaterPic: {
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
      this.scmBGysMaterPic.materId = ''
      this.scmBGysMaterPic.fileId = ''
      this.form.resetFields()
    },
    handleCancleClick () {
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
      //console.info(this.$refs.upfc)
      if (scmBGysMaterPic.fileId) {
        if (scmBGysMaterPic.fileId != '') {
          this.scmBGysMaterPic.fileId = scmBGysMaterPic.fileId
          this.isShow = 0
          this.fileList = []
          this.$get('comFile/' + scmBGysMaterPic.fileId).then((r) => {
            this.$refs.upfc.setFormValue(scmBGysMaterPic.materId + '_' + scmBGysMaterPic.txz01, scmBGysMaterPic.materId)
            let data = r.data
            this.fileList.push({
              uid: data.id,
              name: data.clientName,
              status: 'done',
              url: data.serverName
            })
          })
        }
      }
    }
  },
  computed: {
    show: {
      get: function () {
        return this.editVisiable
      },
      set: function () {
      }
    }
  }
}
</script>
