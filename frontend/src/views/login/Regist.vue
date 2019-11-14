<template>
  <div>
    <a-form :form="form">
      <h2>请仔细填写相关内容</h2>
      <a-form-item
        label="供应商类别"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-select
          v-decorator="[
          'lb',
          { rules: [{ required: true, message: '请选择供应商' }] },
        ]"
          placeholder="请选择供应商"
          @change="handleSelectChange"
        >
          <a-select-option value="0">
            药品供应商
          </a-select-option>
          <a-select-option value="1">
            物资供应商
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
    <medicine-vendor v-show="isVisable == 0">
    </medicine-vendor>
    <mater-vendor v-show="isVisable == 1">
    </mater-vendor>
  </div>
</template>

<script>
import MedicineVendor from './MedicineVendor'
import MaterVendor from './MaterVendor'
export default {
  components: { MedicineVendor, MaterVendor },
  name: 'Regist',
  data () {
    return {
      isVisable: 0,
      formLayout: 'horizontal',
      form: this.$form.createForm(this, { name: 'form' })
    };
  },
  methods: {
    handleSelectChange (value) {
      this.isVisable = value
    }
  }
};
</script>
