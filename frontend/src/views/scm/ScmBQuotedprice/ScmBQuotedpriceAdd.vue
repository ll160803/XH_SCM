<template>
  <a-drawer
    title="报价列表"
    :maskClosable="false"
    width=1200
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="addVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <a-layout>
      <a-layout-header>
        <p style="font-size:18px;">药品名称:{{scmBQueryprice.txz01}} 药品编码：{{scmBQueryprice.matnr}} 规格：{{scmBQueryprice.spec}} 计量单位：{{scmBQueryprice.mseht}} 采购数量：{{scmBQueryprice.amount}}</p>
      </a-layout-header>
      <a-layout-content>
        <a-form :form="form">
          <a-card
            v-for="(item) in quoteList"
            :key="item.index"
            :title="item.name"
            :ref="'quote'+item.index"
          >
            <template slot="extra">
              <a-button
                icon="delete"
                @click="deleteByindex(item.index)"
              >删除该报价</a-button>
              <a-button
                style="margin-left: 8px"
                icon="plus-circle"
                @click="addHos(item)"
              >添加更多参考医院</a-button>
            </template>
            <a-row>
              <a-col :span="8">
                <a-form-item
                  v-bind="formItemLayout"
                  label="供货厂家"
                >
                  <a-input
                    v-model="item.productName"
                    placeholder="请输入供货厂家"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="5">
                <a-form-item
                  v-bind="formItemLayout"
                  label="报价价格"
                >
                  <a-input-number
                    v-model="item.quotedPrice"
                    placeholder="请输入报价价格"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item
                  v-bind="formItemLayout"
                  label="可供数量"
                >
                  <a-input-number
                    v-model="item.supplyAmount"
                    placeholder="请输入供货数量"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="5">
                <a-form-item
                  v-bind="formItemLayout"
                  label="备注"
                >
                  <a-input
                    v-model="item.comments"
                    placeholder="请输入备注"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row
              v-for="(hitem) in item.hospital"
              :key="hitem.displayIndex"
            >
              <a-col :span="10">
                <a-form-item
                  v-bind="formItemLayout"
                  label="该厂家其他供货医院"
                >
                  <a-input
                    v-model="hitem.hospitalName"
                    placeholder="请输入供货医院"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="10">
                <a-form-item
                  v-bind="formItemLayout"
                  label="该医院目前供货价格"
                >
                  <a-input-number
                    v-model="hitem.price"
                    placeholder="请输入供货价格"
                  />
                </a-form-item>
              </a-col>

              <a-col :span="4">
                <a-form-item
                  v-bind="formItemLayout"
                  label="备注"
                >
                  <a-input
                    v-model="hitem.commentsD"
                    placeholder="请输入备注"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </a-card>
          <a-button
            size="large"
            type="dashed"
            icon="plus-square"
            @click="addQuote"
          >新增报价</a-button>
        </a-form>
      </a-layout-content>
    </a-layout>
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
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 13 }
}
export default {
  name: 'ScmBQuotedpriceAdd',
  props: {
    addVisiable: {
      default: false
    },
    scmBQueryprice: {
      required: true
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      quoteList: [
        {
          index: 1,
          name: '报价1',
          productName: '',
          quotedPrice: '',
          supplyAmount: '',
          comments: '',
          hosCount: 1,
          hospital: [
            {
              displayIndex: 1,
              hospitalName: '',
              price: 0,
              commentsD: '',
              comFileId: ''
            }
          ]
        }
      ],
      indexCalc: 1

    }
  },
  methods: {
    reset () {
      this.loading = false
      this.quoteList=[
        {
          index: 1,
          name: '报价1',
          productName: '',
          quotedPrice: '',
          supplyAmount: '',
          comments: '',
          hosCount: 1,
          hospital: [
            {
              displayIndex: 1,
              hospitalName: '',
              price: 0,
              commentsD: '',
              comFileId: ''
            }
          ]
        }
      ]
      this.indexCalc=1
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      let that = this
      let arrJson = JSON.stringify(this.quoteList)
      that.$post('scmBQuotedprice/add', { jsonString: arrJson,baseid: this.scmBQueryprice.id }).then(() => {
        that.$message.success('操作成功')
        that.selectedRowKeys = []
        that.search()
      })
    },
    deleteByindex (index) {
      if (this.quoteList.length > 1) {
        this.quoteList = this.quoteList.filter(p => p.index !== index)
      }
      else {
        this.$message.warning('请至少保留一条报价')
        return
      }
    },
    addQuote () {
      this.indexCalc = this.indexCalc + 1
      this.quoteList.push(
        {
          index: this.indexCalc,
          name: `报价${this.indexCalc}`,
          productName: '',
          quotedPrice: '',
          supplyAmount: '',
          comments: '',
          hosCount: 1,
          hospital: [
            {
              displayIndex: 1,
              hospitalName: '',
              price: 0,
              commentsD: '',
              comFileId: ''
            }
          ]
        }
      )
    },
    addHos (item) {
      item.hosCount += 1
      item.hospital.push({
        displayIndex: item.hosCount,
        hospitalName: '',
        price: 0,
        commentsD: '',
        comFileId: ''
      }
      )
    },
    setFields () {
      let values = this.form.getFieldsValue(['id', 'code', 'baseId', 'productName', 'quotedPrice', 'supplyAmount', 'comments', 'state', 'displayIndex', 'gysname', 'gysaccount', 'isDeletemark', 'createTime', 'modifyTime', 'createUserId', 'modifyUserId'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.scmBQuotedprice[_key] = values[_key] })
      }
    }
  }
}
</script>
<style>
.ant-layout-header {
  background: #fff !important;
}
</style>

