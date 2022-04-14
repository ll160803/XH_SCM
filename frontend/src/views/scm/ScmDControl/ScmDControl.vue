<template>
  <a-card :bordered="false" class="card-area">
    <a-form :form="form">
      <a-row>
        <a-col :md="8" :sm="24">
          <a-form-item
            label="供应商开票截至时间"
            :labelCol="{ span: 8 }"
            :wrapperCol="{ span: 15, offset: 1 }"
          >
            <a-date-picker
              v-decorator="[
                'endTime',
                { rules: [{ required: true, message: '截至时间不能为空' }] },
              ]"
            />
          </a-form-item>
        </a-col>
         <a-col :md="8" :sm="24">
              <a-button type="primary" @click="setting">设置</a-button>
         </a-col>
      </a-row>
    </a-form>
  </a-card>
</template>

<script>
import moment from 'moment'
export default {
  name: 'ScmBGysfpEdit',
  data () {
    return {
      form: this.$form.createForm(this),
      loading: false
    }
  },
  mounted(){
    this.fetch()
  },
  methods: {
    moment,
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    fetch(){
        this.$get('scmDControl', {
      }).then((r) => {
        let rows=  r.data.rows
        if(rows.length>0){
            if(rows[0].endTime!=null) {
            this.form.getFieldDecorator('endTime')
            this.form.setFieldsValue({endTime: moment(rows[0].endTime)})
            }
        }
      });
    },
    setting(){
         this.loading =true
         let scmDControl = this.form.getFieldsValue()
         
          this.$put('scmDControl', {
            endTime: scmDControl.endTime
          }).then(() => {
              this.loading =false
            //this.reset()
             this.$message.success('设置成功.')
          }).catch(() => {
            this.loading = false
          })
    }
  }
}
</script>

<style>
</style>