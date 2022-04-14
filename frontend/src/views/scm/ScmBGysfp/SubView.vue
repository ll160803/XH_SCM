<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <!-- <div ref="lodopDiv"></div>
    <div> -->
    <div>
      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="record => record.id"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        :rowSelection="{type:'radio', selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        :bordered="bordered"
        :expandedRowKeys="expandedRowKeys"
        @expand="expandSubGrid"
      >
        <a-table
          ref="subTable"
          slot="expandedRowRender"
          slot-scope="record"
          :columns="innerColumns"
          :dataSource="record.innerData"
          :pagination="false"
          :rowKey="record2 => record2.id"
        >
        </a-table>
      </a-table>
    </div>
  </a-card>
</template>

<script>
import moment from 'moment'
export default {
  name: 'Sendorder',
  components: {  },
  data () {
    return {
      scroll: {
        x: 1200,
        y: window.innerHeight - 200 - 100 - 30
      },
      advanced: false,
      dataSource: [],
      selectedRowKeys: [],
      expandedRowKeys: [],
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
      addVisiable: false,
      editVisiable: false,
      loading: false,
      bordered: true,
      
    }
  },
  props: {
    fphm: {
        default: ''
    }
  },
  computed: {
    columns () {
      let { sortedInfo } = this
      sortedInfo = sortedInfo || {}
      return [{
        title: '院区',
        dataIndex: 'werkst'
      },{
        title: '库房',
        dataIndex: 'lgortName'
      },{
        title: '开票单号',
        dataIndex: 'id'
      }, {
        title: '开票金额',
        dataIndex: 'fpjr'
      },{
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' }
      }]
    },
    innerColumns () {
      return [{
        title: '供应计划号',
        dataIndex: 'id'
      },
       {
        title: '供应数量',
        dataIndex: 'gMenge'
      }, {
        title: '批号',
        dataIndex: 'charge'
      }, {
        title: '有效期',
        dataIndex: 'vfdat',
        customRender: (text, row, index) => {
          return moment(text).format('YYYY-MM-DD')
        }
      }, {
        title: '生产日期',
        dataIndex: 'hsdat',
        customRender: (text, row, index) => {
          return moment(text).format('YYYY-MM-DD')
        }
      }, {
        title: '发票号码',
        dataIndex: 'fphm'
      }, {
        title: '发票金额',
        dataIndex: 'fpjr'
      }, {
        title: '发票日期',
        dataIndex: 'fprq',
        customRender: (text, row, index) => {
          if(text== null) return ''
          return moment(text).format('YYYY-MM-DD')
        }
      }, {
        title: '状态',
        dataIndex: 'status',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return <a-tag color="purple">未入库</a-tag>
            case 1:
              return <a-tag color="green">已入库</a-tag>
            default:
              return text
          }
        }
      }, {
        title: '预收数量',
        dataIndex: 'doneMenge'
      }, {
        title: '包装规格',
        dataIndex: 'pkgAmount'
      }, {
        title: '包装数量',
        dataIndex: 'pkgNumber'
      }]
    }
  },
  mounted () {
   // this.fetch()
  },
  methods: {
    onSelectChange (selectedRowKeys) {
     // this.getFpjr(selectedRowKeys[0])
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
      if (!this.advanced) {
        this.queryParams.comments = ''
      }
    },
    getFpjr(key){
        console.info(key)
      let data= this.dataSource
      var data2= data.filter(p=>p.id==key)
      console.info(data2)
      this.$emit('sucess',data2[0].fpjr,key)
    },
    setWerks (werks) {
      this.queryParams.werks = werks
    },
    setLgort (lgort) {
      this.queryParams.lgort = lgort
    },
    handleAddClose () {
      this.addVisiable = false
    },
       expandSubGrid (expanded, record) { // 获取供应计划的数量
      if (expanded) {
        this.expandedRowKeys.push(record.id)
        this.handleSubData(record) // 获取子表数据
      } else {
        let expandedRowKeys = this.expandedRowKeys.filter(RowKey => RowKey !== record.id)
        this.expandedRowKeys = expandedRowKeys
      }
    },
    handleSubData (record) {
      this.loading = true
      this.$get('scmBSupplyplan', {
        code: record.id
      }).then((r) => {
        let data = r.data
        this.loading = false
        record.innerData = data.rows
      })
    },
    search (fphm,fpjr) {
      let { sortedInfo } = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.queryParams.fphm = fphm
      this.queryParams.fpjr = fpjr
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams
      })
      this.expandedRowKeys = [] //合并所有展开子项
    },
    reset () {
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
      this.queryParams = {}
      this.fetch()
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
        params.sortField = "ID"
        params.sortOrder = "descend"
      }
     // params.fphm = this.fphm
      this.$get('scmBFpplan/fp', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
