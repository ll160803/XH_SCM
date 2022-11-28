<template>
  <a-drawer
    title="新增"
    :maskClosable="false"
    width="90%"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="addVisiable"
    style="height: calc(100% - 55px); overflow: auto; padding-bottom: 53px"
  >
    <a-form :form="form">
      <a-row>
        <a-col :span="8">
          <a-form-item
            label="院区"
           v-bind="formItemLayout"
          >
            <a-select v-model="queryParams.fyId">
              <a-select-option :value="2000">本院</a-select-option>
              <a-select-option :value="2100">肿瘤</a-select-option>
              <a-select-option :value="2200">西院</a-select-option>
              <a-select-option :value="2300">金银湖</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
         <a-col :span="8">
          <a-form-item
            label="药品名称"
           v-bind="formItemLayout"
          >
            <a-input v-model="queryParams.ypMc">
            </a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4" :offset="1">
          <a-input-search
            placeholder="输入ID号"
            v-model="queryParams.id"
            @search="search"
            enterButton="查询"
          />
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item v-bind="formItemLayout" label="发票号码">
            <a-input
              placeholder="请输入发票号码"
              v-decorator="[
                'fphm',
                { rules: [{ required: true, message: '发票号码不能为空' }] },
              ]"
            />
          </a-form-item>
        </a-col>
         <a-col :span="6">
          <a-form-item v-bind="formItemLayout" label="发票日期">
           <a-date-picker style="width:100%;" v-decorator="[ 'fprq', { rules: [{ required: true, message: '发票日期不能为空' }] }]" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item v-bind="formItemLayout" label="合计金额">
            {{ totalAmount }}
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item v-bind="formItemLayout" label="发票金额">
            <a-input-number
              placeholder="请输入发票金额"
              :precision="2"
              style="width:100%;"
              v-decorator="[
                'fpjrReal',
                { rules: [{ required: true, message: '发票金额不能为空' }] },
              ]"
            />
          </a-form-item>
        </a-col>
       
      </a-row>
    </a-form>
    <a-table
      ref="TableInfo"
      :columns="columns"
      :rowKey="(record) => record.id"
      :dataSource="dataSource"
      :pagination="pagination"
      :loading="loading"
      :rowSelection="{
        selectedRowKeys: selectedRowKeys,
        onChange: onSelectChange,
      }"
      @change="handleTableChange"
      :bordered="bordered"
      :scroll="{ x: 1700, y: 300 }"
    >
    </a-table>
    <div class="drawer-bootom-button">
      <a-popconfirm
        title="确定放弃编辑？"
        @confirm="onClose"
        okText="确定"
        cancelText="取消"
      >
        <a-button style="margin-right: 0.8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading"
        >提交</a-button
      >
    </div>
  </a-drawer>
</template>
<script>
import moment from 'moment'

const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15 }
}
export default {
  name: 'SendorderAdd',
  components: {},
  props: {
    addVisiable: {
      default: false
    }
  },
  watch: {
    // addVisiable () {
    //   if (this.addVisiable) {
    //     this.fetch()
    //   }
    // }
  },
  computed: {
    columns () {
      return [{
        title: '唯一号',
        dataIndex: 'id',
        width: 100
      },
      {
        title: '类别',
        dataIndex: 'ywlx',
        width: 100,
        customRender: (text, row, index) => {
          switch (text) {
            case '1':
              return '草药代煎'
            case '2':
              return '核医学'
            default:
              return text
          }
        },
      },
      {
        title: '业务所属月份',
        dataIndex: 'ny',
        width: 100
      },
      {
        title: '院区',
        dataIndex: 'fyId',
        width: 100,
        customRender: (text, row, index) => {
          switch (text) {
            case '2000':
              return '本院'
            case '2100':
              return '肿瘤'
            case '2200':
              return '西院'
            case '2300':
              return '金银湖'
            default:
              return text
          }
        },
      },
      // {
      //   title: '库位',
      //   dataIndex: 'kw',
      //   width: 100
      // },
      // {
      //   title: '药品类别',
      //   dataIndex: 'ypLb',
      //   width: 100
      // },
      // {
      //   title: '药品编码',
      //   dataIndex: 'ypBh',
      //   width: 100
      // },
      // {
      //   title: '产地代码',
      //   dataIndex: 'ypCd',
      //   width: 100
      // },
      {
        title: '药品名称',
        dataIndex: 'ypMc',
        width: 100
      },
      {
        title: '数量',
        dataIndex: 'sl',
        width: 100
      },
      {
        title: '单位',
        dataIndex: 'dw',
        width: 100
      },
     {
        title: '进货金额',
        dataIndex: 'jhJe',
        width: 100
      },
      {
        title: '创建人姓名',
        dataIndex: 'cjr',
        width: 100
      },
      {
        title: '创建人工号',
        dataIndex: 'cjrid',
        width: 100
      },
      {
        title: '备注',
        dataIndex: 'remark',
        width: 100
      }]
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      scmWChange: {},
      scmWFpplan: {},
      dataSource: [],
      selectedRowKeys: [],
      sortedInfo: null,
      paginationInfo: null,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      queryParams: {

      },
      loading: false,
      bordered: true,
      werks: '',
      lgort: '',
      rows: [],
      totalAmount: 0
    }
  },
  methods: {
    onClose () {
      this.reset()
      this.$emit('close')
    },
    onChange (date, dateString) {
      if (date == null) {
        delete this.queryParams.materCodeFrom
      }
      else {
        this.queryParams.materCodeFrom = dateString
      }
    },
    onChange2 (date, dateString) {
      if (date == null) {
        delete this.queryParams.materCodeTo
      }
      else {
        this.queryParams.materCodeTo = dateString
      }
    },
    onSelectChange (selectedRowKeys) {
      console.info(selectedRowKeys)
      this.selectedRowKeys = selectedRowKeys
      const dataSource = [...this.dataSource];
      for (let item of dataSource) {
        const rows2 = [...this.rows]

        if (this.selectedRowKeys.indexOf(item.id) >= 0) {
          this.rows = rows2.filter(item2 => item2.id !== item.id);
          this.rows.push(item)
        }
        else {
          this.rows = rows2.filter(item2 => item2.id !== item.id);
        }
      }
      this.totalAmount = 0;
      if (this.rows.length > 0) {


        this.totalAmount = this.rows.reduce((a, b) => {
          a = (parseFloat(a) + parseFloat(b.jhJe)).toFixed(2)
          return a
        }, 0)
      }
    },
    setWerks (werks) {
      this.queryParams.werks = werks
    },
    setLgort (lgort) {
      this.queryParams.lgort = lgort
    },
    search () {
      let { sortedInfo } = this
      let sortField, sortOrder

      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }

      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams
      })
    },
    reset () {
      this.loading = false
      this.scmBSendorder = {}
      this.totalAmount = 0
      this.rows = []
      this.form.resetFields()
      // 取消选中
      this.selectedRowKeys = []

      // 重置列排序规则
      this.sortedInfo = null
      // this.paginationInfo = null
      // 重置查询参数

      this.fetch({ id: '-1' })
    },
    handleTableChange (pagination, filters, sorter) {
      this.sortedInfo = sorter
      this.paginationInfo = pagination
      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams
      })
    },
    fetch (params = {}) {
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.pageSize = this.paginationInfo.pageSize
        params.pageNum = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.pagination.defaultPageSize
        params.pageNum = this.pagination.defaultCurrent
      }
      if (params.sortField == null) {
        params.sortField = "id"
        params.sortOrder = "descend"
      }
      params.state = 0 // 为收货的数据
      //console.info(params.sendDeaprtContact)

      this.$get('scmWSale', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['fphm','fpjrReal','fprq'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.scmWFpplan[_key] = values[_key] })
      }
    },
    handleSubmit () {
      let supplyPlanIds = this.selectedRowKeys.join(",")
      this.scmWChange["supplyPlanIds"] = supplyPlanIds
      var werks = ''
      var lgort = ''
      var sendDeaprtContact = ''
      var msg = ''
      for (let item of this.rows) {
        if (werks == '') {
          werks = item.werks;
        }
        else {
          if (werks != item.werks) {
            msg += item.id + ":" + item.werks + "院区不一致"
          }
        }
      }
      if (msg != "") {
        this.$message.warning(msg);
        return
      }

      let that = this;
      that.form.validateFields((err, values) => {
        if (!err) {
          that.setFields()
          that.$post('scmWKpplan/planAdd', {
            ...that.scmWChange,
            ...this.scmWFpplan
          }).then(() => {
            that.reset()
            that.$emit('success')
          }).catch(() => {
            that.loading = false
          })
        }
      })
    },
  }
}
</script>

