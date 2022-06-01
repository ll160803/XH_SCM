<template>
  <div>
    <a-table
      bordered
      :rowKey="(record) => record.fileId"
      :data-source="dataSource"
      :columns="columns"
      :pagination="false"
      size="small"
      :scroll="{ x: 900 }"
    >
      <template slot="fileId" slot-scope="text, record">
        <div v-if="isEdit&&(text==null ||text=='')">
          <tableUpload-file
      @setFileId="(a,b,c)=>setFileId(record,a,b,c)"
    >
    </tableUpload-file>
        </div>
        <div v-else>
         <a
            :href="record.fileUrl"
            v-if="text!=null && text !=''"
            target="_blank"
          >查看</a>
        </div>
      </template>
      <template slot="operation" slot-scope="text, record">
        <a-popconfirm
          v-if="isEdit"
          title="是否删除?"
          @confirm="() => onDelete(record)"
        >
          <a-button style="width: 80px">删除</a-button>
        </a-popconfirm>
        <a-button v-if="isEdit" style="width: 80px" type="dashed" block @click="handleAdd()">
          新增
        </a-button>
      </template>
    </a-table>
   
  </div>
</template>

<script>
import TableUploadFile from '../../common/TableUploadFile'
export default {
  name: "XxbBCheckD",
  props: {
      isEdit: {
        default: true
      }
  },
  data () {
    return {
      dataSource: [{
        fileId: '',
        fileUrl: ''
      }],
      editRecord: {},
      baseId: "",
     
      fileVisiable: false,
      // baseUserYggh: null,
      columns: [
        {
          title: "文件名",
          dataIndex: "clientName",
          width: 200,
        },
        {
          title: "上传",
          dataIndex: "fileId",
          scopedSlots: {
            customRender: "fileId",
          },
          width: 160,
        },
        {
          title: "操作",
          dataIndex: "operation",
          scopedSlots: {
            customRender: "operation",
          },
          width: 200,
        },
      ],
    };
  },
  components: { TableUploadFile },
  mounted () { },
  methods: {
    OpenFile (record) {
      this.editRecord = record
      this.fileVisiable = true
      if (record.fileId != undefined && record.fileId != '') {
        this.$refs.upFile.fetch(record.fileId)
      }
    },
    setFileId (record,fileId, fileUrl, fileName) {

      console.log(fileUrl)
      /**
       const dataSource = [...this.dataSource]
       console.log(this.editRecord.id)
       let record=dataSource.filter(p=>p.id===this.editRecord.id)
       console.log(record)*/
     record["fileId"] = fileId
      record["fileUrl"] = fileUrl
     record["clientName"] = fileName
      //this.dataSource =[...dataSource]
    },
    reset () {
      this.baseId = null
      this.dataSource = [{
        fileId: '',
        fileUrl: ''
      }]
    },

    onDelete (obj) {
      const index = this.dataSource.indexOf(obj)
      const newList = this.dataSource.slice()
      newList.splice(index, 1)
      this.dataSource = newList
      if (newList.length == 0) {
        this.handleAdd()
      }
    },

    handleAdd () {
      let checkD = {
        fileId: '',
        fileUrl: ''
      }
      this.addDataSource(checkD);
    },
    addDataSource (obj) {
      const { dataSource } = this;
      const newData = obj;
      this.dataSource = [newData, ...dataSource];
    },
    getAllFileId(){
      const { dataSource } = this;
      let ids= []
      dataSource.forEach(p=>{
         ids.push(p.fileId)
      })
      return ids;
    },
    fetch(baseId){
      this.$get('comFile/list', {
        refTabId: baseId,
        refTabTable: 'scmbgysfp'
      }).then((r) => {
        let data = r.data
        let list= []
        if(data.length>0){
          data.forEach(item=>{
          list.push({
            fileId: item.id,
            fileUrl: this.$baseUrl+"uploadFile/"+item.serverName,
            clientName: item.clientName
          })
          })
          this.dataSource = list;
        }
        
      }
      )
    }
  },
};
</script>

<style scoped>
.editable-cell {
  position: relative;
}

.editable-cell-input-wrapper,
.editable-cell-text-wrapper {
  padding-right: 24px;
}

.editable-cell-text-wrapper {
  padding: 5px 24px 5px 5px;
}

.editable-cell-icon,
.editable-cell-icon-check {
  position: absolute;
  right: 0;
  width: 20px;
  cursor: pointer;
}

.editable-cell-icon {
  line-height: 18px;
  display: none;
}

.editable-cell-icon-check {
  line-height: 28px;
}

.editable-cell:hover .editable-cell-icon {
  display: inline-block;
}

.editable-cell-icon:hover,
.editable-cell-icon-check:hover {
  color: #108ee9;
}

.editable-add-btn {
  margin-bottom: 8px;
}
</style>
