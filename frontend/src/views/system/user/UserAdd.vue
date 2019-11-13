<template>
  <a-drawer
    title="新增用户"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="userAddVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <!-- 新增用户 -->
    <my-area
      ref="aduser"
      :myAreaVisiable="true"
    >
    </my-area>
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
import MyArea from './MyArea'
export default {
  components: { MyArea },
  name: 'UserAdd',
  props: {
    userAddVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false
    }
  },
  methods: {
    reset () {
      this.$refs.aduser.reset()
      this.loading = false
    },
    onClose () {
      this.loading = false
      this.$refs.aduser.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.$refs.aduser.handleValues()
      this.loading = true
      this.$post('user', {
        ...this.$refs.aduser.user
      }).then((r) => {
        this.reset()
        this.$emit('success')
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>
