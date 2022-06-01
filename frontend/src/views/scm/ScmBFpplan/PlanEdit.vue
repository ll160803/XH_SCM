
<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width="90%"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-row>
        <a-col :span="8">
          <werks-lgort
            ref="werklgort"
            @werks="setWerks"
            @lgort="setLgort"
          >
          </werks-lgort>
        </a-col>
           <a-col
          :span="4"
          :offset="1"
        >
          <a-input
            placeholder="输入药品编码"
            v-model="queryParams.matnr"
          />
        </a-col>
         <a-col
          :span="4"
          :offset="1"
        >
          <a-input
            placeholder="输入药品名称"
            v-model="queryParams.txz01"
          />
        </a-col>
        <a-col
          :span="3"
          :offset="1"
        >
        <a-form-item
          label="是否集采"
          :labelCol="{span: 12}"
          :wrapperCol="{span:12}"
        >
          <a-select
            v-model="queryParams.sendDeaprtContact"
            style="width: 100%"
            :dropdownStyle="{width: '200%'}"
          >
            <a-select-option
             value="1"
             key="1"
            >否</a-select-option>
             <a-select-option
             value="0"
             key="0"
            >是</a-select-option>
          </a-select>
        </a-form-item>
        </a-col>
            <a-col :span="5" :offset="1">
              <a-form-item
                label="入账开始时间"
                :labelCol="{ span: 8 }"
                
                :wrapperCol="{ span: 15, offset: 1 }"
              >
                <a-date-picker style="width:100%" @change="onChange"  />
              </a-form-item>
            </a-col>
           <a-col :span="5" :offset="1">
              <a-form-item
                label="入账结束时间"
               
                :labelCol="{ span: 8 }"
                :wrapperCol="{ span: 15, offset: 1 }"
              >
                <a-date-picker  style="width:100%" @change="onChange2"  />
              </a-form-item>
            </a-col>
        <a-col
          :span="4"
          :offset="1"
        >
          <a-input-search
            placeholder="输入供应计划号"
            v-model="queryParams.id"
            @search="search"
            enterButton="查询"
          />
        </a-col>
      </a-row>

    </a-form>
    <a-table
      ref="TableInfo"
      :columns="columns"
      :rowKey="record => record.id"
      :dataSource="dataSource"
      :pagination="pagination"
      :loading="loading"
      :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
      @change="handleTableChange"
      :bordered="bordered"
      :scroll="{ x: 1700 ,y:300 }"
    >
    </a-table>
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
import moment from 'moment'
import WerksLgort from '../../common/WerksLgort'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15 }
}
export default {
  name: 'SendorderEdit',
  props: {
    editVisiable: {
      default: false
    },
    orderId: '',
    fphm: ''
  },
  components: { WerksLgort },
  watch: {
    editVisiable () {
      if (this.editVisiable) {
       // this.fetch({})
        this.getPlanIds()
      }
    }
  },
  computed: {
    columns () {
      return [{
        title: '供应计划号',
        dataIndex: 'id',
        width: 100
      },{
        title: '是否集中采购',
        dataIndex: 'sendDeaprtContact',
        customRender: (text, row, index) => {
          if(text=='0') return '是'
          return '否'
        },
        width: 100
      }, {
        title: '药品编码',
        dataIndex: 'matnr',
        width: 100
      }, {
        title: '药品名称',
        dataIndex: 'txz01',
        width: 200
      }, {
        title: '供应数量',
        dataIndex: 'gMenge',
        width: 80
      }, {
        title: '批号',
        dataIndex: 'charge',
        width: 80
      }, {
        title: '有效期',
        dataIndex: 'vfdat',
        customRender: (text, row, index) => {
          return moment(text).format('YYYY-MM-DD')
        },
        width: 100
      }, {
        title: '生产日期',
        dataIndex: 'hsdat',
        customRender: (text, row, index) => {
          if(text==null) return ''
          return moment(text).format('YYYY-MM-DD')
        },
        width: 100
      }, {
        title: '发票号码',
        dataIndex: 'fphm',
        width: 100
      }, {
        title: '发票金额',
        dataIndex: 'fpjr',
        width: 80
      }, {
        title: '发票日期',
        dataIndex: 'fprq',
        customRender: (text, row, index) => {
          if(text== null) return ''
          return moment(text).format('YYYY-MM-DD')
        },
        width: 100
      }, {
        title: '状态',
        dataIndex: 'status',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return <a-tag color="purple">未收货</a-tag>
            case 1:
              return <a-tag color="green">已收货</a-tag>
            default:
              return text
          }
        },
        width: 80
      }, {
        title: '包装规格',
        dataIndex: 'pkgAmount',
        width: 80
      }, {
        title: '包装数量',
        dataIndex: 'pkgNumber',
        width: 80
      }]
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      scmBFpplan: {},
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
        sendDeaprtContact: '1'
      },
      loading: false,
      bordered: true
    }
  },
  methods: {
    onClose () {
      this.reset()
      this.$emit('close')
    },
   onChange (date, dateString) {
      if(date==null) {
        delete this.queryParams.materCodeFrom
      }
      else{
      this.queryParams.materCodeFrom = dateString
      }
    },
    onChange2 (date, dateString) {
        if(date==null) {
           delete this.queryParams.materCodeTo
        }
        else{
      this.queryParams.materCodeTo = dateString
        }
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
      //   const dataSource = [...this.dataSource]
      //   let row = dataSource.find(item => item.id === selectedRowKeys[0])
      //   this.issue_content = `${row.matnr}`
      //   this.matnr = row.matnr
      //   this.isSelect = 1
      //   this.serch_result_issue = false
      //   this.$emit("sucess", { maktx: row.maktx, meins: row.meins, mseht: row.mseht })
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
      this.scmBFpplan = {}
      
      this.form.resetFields()
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列排序规则
      this.sortedInfo = null
      this.paginationInfo = null
      // 重置查询参数
      this.queryParams = {
        sendDeaprtContact: '1'
      }
      this.$refs.werklgort.reset()
      this.fetch({id: '-1'})
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
    getPlanIds () {
      this.$get('scmBFpplan/planIds/' + this.orderId, {

      }).then((r) => {
        let data = r.data.data
        console.info(data);
        for (let element of data) {
          console.info(element);
          this.selectedRowKeys.push(element);
        }
        //this.selectedRowKeys = data;
      })
    },
    setWerks (werks) {
      this.queryParams.werks = werks
    },
    setLgort (lgort) {
      this.queryParams.lgort = lgort
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
      params.fphm = this.fphm
      params.code = this.orderId
      params.status = 1 //未收货的数据
      
      this.$get('viewSupplyplan/code', {
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
    handleSubmit () {
      let supplyPlanIds = this.selectedRowKeys.join(",")
      this.scmBFpplan["supplyPlanIds"] = supplyPlanIds
      this.form.validateFields((err, values) => {
        if (!err) {
          //this.setFields()
          this.$put('scmBFpplan/orderEdit', {
            ...this.scmBFpplan
          }).then(() => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['sendDate'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.scmBFpplan[_key] = values[_key] })
      }
    },
    setFormValues (id) {
      this.scmBFpplan.id = id
    }
  }
}
</script>

