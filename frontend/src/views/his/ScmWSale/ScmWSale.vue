<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <a-row>
          <div :class="advanced ? null : 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item label="业务类型" v-bind="formItemLayout">
                <a-select v-model="queryParams.ywlx">
                   <a-select-option :value="-1">全部
                </a-select-option>
                <a-select-option :value="1">草药代煎
                </a-select-option>
                 <a-select-option :value="2">核医学
                </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="24">
              <a-form-item label="业务月份" v-bind="formItemLayout">
                <a-input v-model="queryParams.ny"/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
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
         <a-col :span="5">
          <a-form-item
            label="供应商名称"
           v-bind="formItemLayout"
          >
           <a-input v-model="queryParams.gysName" />
          </a-form-item>
        </a-col>
         <a-col :span="5">
          <a-form-item
            label="唯一码"
           v-bind="formItemLayout"
          >
           <a-input v-model="queryParams.id" />
          </a-form-item>
        </a-col>
          </div>
          <span style="float: right; margin-top: 3px">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
            <a @click="toggleAdvanced" style="margin-left: 8px">
              {{ advanced ? "收起" : "展开" }}
              <a-icon :type="advanced ? 'up' : 'down'" />
            </a>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
      <a-button
          type="primary"
          @click="exportExcel"
        >导出</a-button>
      </div>
      <!-- 表格区域 -->
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
        :scroll="{ x: 900 }"
      >
        <template slot="remark" slot-scope="text, record">
          <a-popover placement="topLeft">
            <template slot="content">
              <div style="max-width: 200px">{{ text }}</div>
            </template>
            <p style="width: 200px; margin-bottom: 0">{{ text }}</p>
          </a-popover>
        </template>
      
      </a-table>
    </div>
    <!-- 新增字典 -->
   
  </a-card>
</template>

<script>


const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'ScmWSale',
  components: { },
  data () {
    return {
      advanced: false,
      dataSource: [],
      selectedRowKeys: [],
      sortedInfo: null,
      paginationInfo: null,
      formItemLayout,
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
      bordered: true
    }
  },
  computed: {
    columns () {
      let { sortedInfo } = this
      sortedInfo = sortedInfo || {}
      return [{
        title: '唯一号',
        dataIndex: 'id',
        width: 100
      },
      {
        title: '类型',
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
      {
        title: '供应商',
        dataIndex: 'gysId',
        width: 100
      },
      {
        title: '供应商名称',
        dataIndex: 'gysName',
        width: 100
      },
      {
        title: '药品编码',
        dataIndex: 'ypBh',
        width: 100
      },
      {
        title: '产地代码',
        dataIndex: 'ypCd',
        width: 100
      },
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
      },
    ]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
      if (!this.advanced) {
        this.queryParams.comments = ''
      }
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
    handleEditSuccess () {
      this.editVisiable = false
      this.$message.success('修改成功')
      this.search()
    },
    handleEditClose () {
      this.editVisiable = false
    },
    edit (record) {
      this.$refs.scmWSaleEdit.setFormValues(record)
      this.editVisiable = true
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
          let scmWSaleIds = that.selectedRowKeys.join(',')
          that.$delete('scmWSale/' + scmWSaleIds).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          }
          )
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
      this.$export('scmWSale/excel', {
        sortField: sortField,
        sortOrder: sortOrder,
        pageNum: 1,
        pageSize:10000,
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
      if(params.ywlx=="-1"){
        delete params.ywlx
      }
      this.$get('scmWSale/all', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      }
      )
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
