<template>
     <a-card
    :bordered="false"
    class="card-area"
  >
   <a-row>
    <a-col :span="12" style="padding:5px;" >
        <a-input
        v-model="planids" placeholder="供应计划号，逗号分隔"
        ></a-input>
    </a-col></a-row>
    <a-row>
      <a-col :span="6" :offset="1" style="padding:5px;">
        <a-input
        v-model="materCode" 
        placeholder="过账日期，比如2019-09-28,为空则清空"
        ></a-input>
    </a-col>
    <a-col :span="6" style="padding:5px;">
        <a-button
        @click="handleMaterCode"
        type="primary"
        :loading="loading"
      >修改过账日期</a-button>
    </a-col>
   </a-row>
   <a-row>
    <a-col :span="8">
        <import-excel
          templateUrl="scmBPurcharseorder/downTemplate"
          @succ="handleRefesh"
          url="scmBPurcharseorder/import"
        >
        </import-excel>
    </a-col>
   </a-row>
   </a-card>
</template>

<script>
import ImportExcel from "../../common/ImportExcel";
export default {
  data () {
    return {
        loading: false,
        materCode: '',
        planids: ''
    }
  },
  components: {ImportExcel},
  methods: {
    handleMaterCode(){
         this.$post('scmBSupplyplan/materCode', {
            materCode: this.materCode,
            ids: this.planids
          }).then(() => {
         this.$message.success('成功修改')
          }).catch(() => {
            this.loading = false
          })
    },
    handleRefesh() {
         this.$message.success('编辑成功')
    }
  }
}
</script>

<style>

</style>