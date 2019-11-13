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
    <div>
      <a-steps :current="current">
        <a-step
          title="基本信息"
          key="base"
        />
        <a-step
          title="用户信息"
          key="base"
        />
      </a-steps>
      <!-- 新增用户 -->
      <my-area
        ref="aduser"
        :myAreaVisiable="true"
        v-show="current == 0"
      >
      </my-area>
    </div>
    <div class="drawer-bootom-button">
      <a-button
        v-if="current < 1"
        type="primary"
        @click="next"
      >
        下一步
      </a-button>
      <a-button
        v-if="current>0"
        style="margin-left: 8px"
        @click="prev"
      >
        上一步
      </a-button>
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
        v-if="current == 1"
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
      loading: false,
      current: 0
    }
  },
  methods: {
    reset () {
      this.$refs.aduser.reset()
      this.loading = false
      this.current = 0
    },
    onClose () {
      this.loading = false
      this.reset()
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
    },
    next () {
      this.current++;
    },
    prev () {
      this.current--;
    }
  }
}
</script>
