<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <!-- <div ref="lodopDiv"></div>
    <div> -->
      <a-form layout="horizontal">
        <div :class="advanced ? null: 'fold'">
          <a-row>
            <a-col
              :md="6"
              :sm="24"
            >
              <a-form-item
                label="开票单号"
                :labelCol="{span: 8}"
                :wrapperCol="{span: 15, offset: 1}"
              >
                <a-input v-model="queryParams.id" />
              </a-form-item>
            </a-col>
             <a-col  :md="6"
              :sm="24">
              <a-form-item
                label="创建开始时间"
                :labelCol="{ span: 8 }"
                :wrapperCol="{ span: 15, offset: 1 }"
              >
                <a-date-picker style="width:100%" @change="onChange"  />
              </a-form-item>
            </a-col>
           <a-col  :md="6"
              :sm="24">
              <a-form-item
                label="创建结束时间"
               
                :labelCol="{ span: 8 }"
                :wrapperCol="{ span: 15, offset: 1 }"
              >
                <a-date-picker  style="width:100%" @change="onChange2"  />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
          <a-form-item
            label="供应商开票截至时间"
            :labelCol="{ span:12 }"
            :wrapperCol="{ span: 11, offset: 1 }"
          >
           <a-input v-model="time"  disabled/>
          </a-form-item>
        </a-col>

          </a-row>

        </div>
        <span style="float: right; margin-top: 3px;">
          <a-button
            type="primary"
            @click="search"
          >查询</a-button>
          <a-button
            style="margin-left: 8px"
            @click="reset"
          >重置</a-button>
        </span>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button
          type="primary"
          ghost
          @click="add"
        >新增</a-button>
         <a-button
          type="primary"
          ghost
          @click="print"
        >打印开票清单</a-button>
        <a-button
          @click="batchDelete"
        >删除</a-button>
      </div>
      <!-- 表格区域 -->
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
        :expandedRowKeys="expandedRowKeys"
        @expand="expandSubGrid"
      >
        <template
          slot="operation"
          slot-scope="text, record"
        >
          <a-icon
           
            type="setting"
            theme="twoTone"
            twoToneColor="#4a9ff5"
            @click="edit(record)"
            title="修改"
          ></a-icon>
        </template>
        <a-table
          ref="subTable"
          slot="expandedRowRender"
          slot-scope="record"
          :columns="innerColumns"
          :dataSource="record.innerData"
          :pagination="false"
          :rowKey="record2 => record2.id2"
        >
          <!-- <template
            slot="operation2"
            slot-scope="text, record2"
          >
            <a-icon
              type="delete"
              theme="twoTone"
              twoToneColor="#4a9ff5"
              @click="subDelete(record2)"
              title="删除"
            ></a-icon>
          </template> -->
        </a-table>
      </a-table>
    </div>
   

    <plan-add
      @close="handleAddClose"
      @success="handleAddSuccess"
      :addVisiable="addVisiable">
    </plan-add>
    <plan-edit 
      ref="scmBSendorderEdit"
      @close="handleEditClose"
      @success="handleEditSuccess"
      :editVisiable="editVisiable"
      :orderId="orderId"
      :fphm="fphm"
      >
    </plan-edit>
    <!-- 修改字典 -->
    <fpplan-print
    ref="sendinfoPrint"
      @close="handlePrintClose"
      :printVisiable="printVisiable"
      :ids="printIds"
      :lodop="lodop">
    </fpplan-print>
  </a-card>
</template>

<script>
import moment from 'moment'
import PlanAdd from './PlanAdd.vue'
import PlanEdit from './PlanEdit.vue'
import { getLodop, getLodopDiv } from '../../../tools/lodop'
import FpplanPrint from './FpplanPrint.vue'

export default {
  name: 'Sendorder',
  components: { PlanAdd, PlanEdit, FpplanPrint },
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
      queryParams: {},
      addVisiable: false,
      editVisiable: false,
      loading: false,
      bordered: true,
      fph: '',
      printIds: '',
      printVisiable: false,
      orderId: '',
      fphm: '',
      time: '',
      lodop: {}
    }
  },
  computed: {
    columns () {
      let { sortedInfo } = this
      sortedInfo = sortedInfo || {}
      return [{
        title: '院区',
        dataIndex: 'werkst'
      }, {
        title: '库房',
        dataIndex: 'lgortName'
      }, {
        title: '发票号码',
        dataIndex: 'fphm'
      }, {
        title: '开票单号',
        dataIndex: 'id'
      }, {
        title: '开票金额',
        dataIndex: 'fpjr'
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' }
      }]
    },
    innerColumns () {
      return [{
        title: '供应计划号',
        dataIndex: 'id2'
      }, {
        title: '药品编码',
        dataIndex: 'matnr'
      }, {
        title: '药品名称',
        dataIndex: 'txz01'
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
          if (text == null) return ''
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
              if (row.materCode != null && row.materCode != '') {
                return <a-tag color="orange">已入帐</a-tag>
              }
              return <a-tag color="green">已入库</a-tag>
            default:
              return text
          }
        }
      }, 
      //{
      //   title: '预收数量',
      //   dataIndex: 'doneMenge'
      // }, {
      //   title: '包装规格',
      //   dataIndex: 'pkgAmount'
      // }, {
      //   title: '包装数量',
      //   dataIndex: 'pkgNumber'
      // }
      ]
    }
  },
  mounted () {
    this.fetch()
    this.fetch2()
  },
  methods: {
    fetch2 () {
      this.$get('scmDControl', {
      }).then((r) => {
        let rows = r.data.rows
        if (rows.length > 0) {
          if (rows[0].endTime != null) {
            this.time = rows[0].endTime.substr(0, 10)
          }
        }
      });
    },
    onChange (date, dateString) {
      if (date == null) {
        delete this.queryParams.createTimeFrom
      }
      else {
        this.queryParams.createTimeFrom = dateString
      }
    },
    onChange2 (date, dateString) {
      if (date == null) {
        delete this.queryParams.createTimeTo
      }
      else {
        this.queryParams.createTimeTo = dateString
      }
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
      if (!this.advanced) {
        this.queryParams.comments = ''
      }
    },
    setWerks (werks) {
      this.queryParams.werks = werks
    },
    setLgort (lgort) {
      this.queryParams.lgort = lgort
    },
    handleAddSuccess () {
      this.addVisiable = false
      this.$message.success('新增成功')
      this.search()
    },
    handleAddClose () {
      this.addVisiable = false
    },
    add () {
      this.addVisiable = true
    },
    print () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要打印的记录')
        return
      }
      if (this.selectedRowKeys.length > 1) {
        this.$message.warning('请选择一条需要打印的记录')
        return
      }

      this.printIds = this.selectedRowKeys.join(',')
      getLodopDiv(this.$refs.lodopDiv)
      if (getLodop() == undefined || getLodop() == null) {

      }
      else {
        this.lodop = getLodop();
        this.printVisiable = true
      }
    },
    handlePrintClose () {
      this.printVisiable = false
    },
    handleEditSuccess () {
      this.editVisiable = false
      this.$message.success('修改成功')
      this.search()
    },
    handleEditClose () {
      this.editVisiable = false
    },
    edit (record) {
      let that = this
      this.orderId = record.id
      this.fphm = record.fphm
      this.editVisiable = true
      setTimeout(function () {
        that.$refs.scmBSendorderEdit.setFormValues(record.id)
      }, 100);
    },
    subDelete (record, pRecord) {
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，此记录将会被彻底删除',
        centered: true,
        onOk () {
          that.$delete('scmBSupplyplan/deleteFpPlan/' + record.id).then((r) => {
            console.log(r)
            if (r.data == null) {
              that.$message.success('删除成功')
            }
            else {
              that.$message.success(r.data.data)
            }
            that.search()
            that.expandedRowKeys = []
          })
        },
        onCancel () {

        }
      })
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
      this.$get('viewSupplyplanNew/fpcode', {
        pageNum: 1,
        pageSize: 1000,
        code: record.id
      }).then((r) => {
        let data = r.data
        this.loading = false
        record.innerData = data.rows
      })
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let scmBSendorderIds = that.selectedRowKeys.join(',')
          that.$delete('scmBFpplan/' + scmBSendorderIds).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    exportExcel () {
      let { sortedInfo } = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.$export('scmBSendorder/excel', {
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams
      })
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
      this.$get('scmBFpplan', {
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
