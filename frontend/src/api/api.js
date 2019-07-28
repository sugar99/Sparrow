import axios from 'axios'

export const GET = "get";
export const POST = "post";
export const PUT = "put"
export const PATCH = "patch"
export const DELETE = "delete"

axios.defaults.withCredentials = true;

// export const baseUrl = "http://192.168.43.211:8089/v1";
// export const baseUrl = "http://192.168.43.211:8080/v1";
// export const baseUrl = "http://localhost:8080/v1";
export const baseUrl = "http://39.108.210.48:8089/v1";
// export const baseUrl = "http://39.108.210.48:18090/v1";
// export const baseUrl = "http://localhost/v1";

// 用户模块
// U1	新建用户
export const newUser = (work_no, username,password,email) => {
	return new axios({
		url: baseUrl + '/users',
		data: {
			'work_no': work_no,
			'username':username,
			'password': password,
			'email':email
		},
		method: POST
	})
};

// U2	用户登录
export const Login = (work_no, password) => {
	return new axios({
		url: baseUrl + '/login',
		data: {
			'work_no': work_no,
			'password': password
		},
		method: POST
	})
};

// U3	获取用户元数据
export const getUser = () => {
	return new axios({
		url: baseUrl + '/users/current',
		method: GET
	})
};

// U4	注销登录
export const Logout = () => {
	return new axios({
		url: baseUrl + '/users/logout',
		method: POST
	})
};

// U5 获取用户所在群组
// export const getGroupOfUser = (user_id) => {
// 	return new axios({
// 		url: baseUrl + '/v1/users/' + user_id + '/groups',
// 		method: GET
// 	})
// };
export const getGroupOfUser = () => {
	return new axios({
		url: baseUrl + '/v1/users/current/groups',
		method: GET
	})
};

// 检索相关

// S1	搜索建议
export const Suggestions = (type, keyword, size) => {
	return new axios({
		url: baseUrl + '/search/suggestions?type=' + type + '&keyword=' + keyword + '&size=' + size,
		method: GET
	})
};

// S2	获取高度相关的类目标签
export const Associations = (keyword, tag_count, category_count) => {
	return new axios({
		url: baseUrl + '/search/associations?keyword=' + keyword + '&tag_count=' + tag_count + '&category_count=' +
			category_count,
		method: GET
	})
};

// S3	搜索结果
export const Results = (type, keyword, tags, categories, exts, created_time, modified_time, time_zone, page, per_page) => {
	return new axios({
		url: baseUrl + '/search/results',
		data: {
			'type': type,
			'keyword': keyword,
			'tags': tags,
			'categories': categories,
			'exts': exts,
			'created_time': created_time,
			'modified_time': modified_time,
			'time_zone': time_zone,
			'page': page,
			'per_page': per_page,
			'desc_highlight_count': 2,
			'content_highlight_count': 5,
			'highlight_tag': 'em'
		},
		method: POST
	})
};

// S4	搜索类目或标签
export const Tags = (keyword, size) => {
	return new axios({
		url: baseUrl + '/search/tags?keyword=' + keyword + '&size=' + size,
		method: GET
	})
};
export const Categories = (keyword, size) => {
	return new axios({
		url: baseUrl + '/search/categories?keyword=' + keyword + '&size=' + size,
		method: GET
	})
};

// 资源类型: dir doc file
// 目录文档部分相关

// D1 统一	新建资源
export const newResource = (type, resource_id) => {
	return new axios({
		url: baseUrl + '/' + type + 's',
		data: {
			'cur_id': resource_id
		},
		method: POST
	})
};
// D1.1		新建目录
export const newDir = (cur_id) => {
	return new axios({
		url: baseUrl + '/dirs',
		data: {
			'cur_id': cur_id
		},
		method: POST
	})
};
// D1.2	新建文档
export const newDoc = (cur_id) => {
	return new axios({
		url: baseUrl + '/docs',
		data: {
			'cur_id': cur_id
		},
		method: POST
	})
};

// D2.1 获取目录元数据  暂时没用
export const getDirMeta = (dir_id) => {
	return new axios({
		url: baseUrl + '/dirs/' + dir_id,
		method: GET
	})
};
// D2.2	获取文档元数据
export const getDocMeta = (doc_id) => {
	return new axios({
		url: baseUrl + '/docs/' + doc_id,
		method: GET
	})
};

// D3.1	更新目录名称
export const editDirMeta = (dir_id, title) => {
	return new axios({
		url: baseUrl + '/dirs/' + dir_id,
		data: {
			'title': title
		},
		method: PUT
	})
};
// D3.2	更新文档元数据
export const editDocMeta = (doc_id, title, desc) => {
	return new axios({
		url: baseUrl + '/docs/' + doc_id,
		data: {
			'title': title,
			'desc': desc
		},
		method: PATCH
	})
};

// D4统一	删除资源
export const deleteResource = (type, dir_id) => {
	return new axios({
		url: baseUrl + '/' + type + 's/' + dir_id,
		method: DELETE
	})
};
// D4.1	删除目录
export const deleteDir = (dir_id) => {
	return new axios({
		url: baseUrl + '/dirs/' + dir_id,
		method: DELETE
	})
};
// D4.2	删除文档
export const deleteDoc = (doc_id) => {
	return new axios({
		url: baseUrl + '/docs/' + doc_id,
		method: DELETE
	})
};

// D5 统一	获取下级资源
export const getResources = (type, resource_id) => {
	return new axios({
		url: baseUrl + '/' + type + 's/' + resource_id + '/slaves',
		method: GET
	})
}
// D5.1	获取指定目录下的子目录或文档
export const getDir = (dir_id) => {
	return new axios({
		url: baseUrl + '/dirs/' + dir_id + '/slaves',
		method: GET
	})
};
// D5.2	获取指定文档下的文件
export const getDoc = (doc_id) => {
	return new axios({
		url: baseUrl + '/docs/' + doc_id + '/slaves',
		method: GET
	})
};

// 文件相关
// F6	删除文件
export const deleteFile = (file_id) => {
	return new axios({
		url: baseUrl + '/files',
		data: {
			"file_id": [
				file_id
			]
		},
		method: DELETE
	})
};

// F7	下载文件 
export const Download = (file_id) => {
	return new axios({
		url: baseUrl + '/files/' + file_id + '/download',
		method: GET
	})
};

// F9	获取文件meta
export const getFileMeta = (file_id) => {
	return new axios({
		url: baseUrl + '/files/' + file_id,
		method: GET
	})
};

// F10	更新文件meta
export const editFileMeta = (file_id, title, desc, categories, tags) => {
	return new axios({
		url: baseUrl + '/files/' + file_id,
		data: {
			'title': title,
			'desc': desc,
			'categories': categories,
			'tags': tags
		},
		method: PATCH
	})
};



// F15 获取文件标签 或 类目
export const getFileCategories = (file_id) => {
	return new axios({
		url: baseUrl + '/files/' + file_id + '/categories',
		method: GET
	})
};

export const getFileTags = (file_id) => {
	return new axios({
		url: baseUrl + '/files/' + file_id + '/tags',
		method: GET
	})
};

// 群组相关
// G1
export const newGroup = (group_name, group_desc) => {
	return new axios({
		url: baseUrl + '/groups/',
		data: {
			'group_name': group_name,
			'group_desc': group_desc
		},
		method: POST
	})
};

// G2
export const getGroupMeta = (group_id) => {
	return new axios({
		url: baseUrl + '/groups/' + group_id,
		method: GET
	})
};

// G3
export const updateGroupMeta = (group_id, group_name, group_desc) => {
	return new axios({
		url: baseUrl + '/groups/' + group_id,
		data: {
			'group_name': group_name,
			'group_desc': group_desc
		},
		method: PUT
	})
};

// G4
export const deleteGroup = (group_id) => {
	return new axios({
		url: baseUrl + '/groups/' + group_id,
		method: DELETE
	})
};

// G5 
export const addUserToGroup = (group_id, usersIdList) => {
	return new axios({
		url: baseUrl + '/groups/' + group_id + '/members',
		data: {
			'usersIdList': usersIdList
		},
		method: POST
	})
};

// G6
export const getUserOfGroup = (group_id) => {
	return new axios({
		url: baseUrl + '/groups/' + group_id + '/members',
		method: GET
	})
};

// G7
export const deleteUserOfGroup = (group_id, member_id) => {
	return new axios({
		url: baseUrl + '/groups/' + group_id + '/members/' + member_id,
		method: DELETE
	})
};

// 权限部分
// P1	添加群组对指定目录或文档的操作权限
export const addPermission = (resource_id, permission, groupsIdList) => {
	return new axios({
		url: baseUrl + '/resources/' + resource_id + '/permissions',
		data: {
			'permission': permission,
			'groupsIdList': groupsIdList
		},
		method: POST
	})
};

// P2	撤销群组对指定目录或文档的操作权限
export const deletePermission = (resource_id, group_id) => {
	return new axios({
		url: baseUrl + '/resources/' + resource_id + '/permissions',
		data: {
			'group_id': group_id
		},
		method: DELETE
	})
};

// P3	获取对指定资源有操作权限的群组信息
export const getGroupOfResourceHadPermission = (resource_id) => {
	return new axios({
		url: baseUrl + '/resources/' + resource_id + '/authgroups',
		method: GET
	})
};

// P4  获取群组具有操作权限的所有资源信息 暂时不用
