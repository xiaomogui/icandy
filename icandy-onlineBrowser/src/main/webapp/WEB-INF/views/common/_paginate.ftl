<#macro paginate currentPage totalPage>
	<#if (totalPage <= 0) || (currentPage > totalPage)><#return></#if>
	<#local pages =sysKit.getPageInfo(currentPage,totalPage,7)>
	<ul class="pagination">
		<#if (currentPage == 1)>
			<li class="disabled"><span>上页</span></li>
		<#else>
			<li><a href="javascript:void(0);"page="${currentPage - 1}">上页</a></li>
		</#if>
		<#assign pre=1>
		<#list pages as i>
			<#if !i_has_next && (i!=1) && pre!=(i-1)><li class="disabled"><span>…</span></li></#if>
			<#if i_index==1 && (pre+1)!=i><li class="disabled"><span>…</span></li></#if>
			<#if i == currentPage>
				<li class="active"><span>#{i}</span></li>
			<#else>
				<li><a href="javascript:void(0);"page="${i}">#{i}</a></li>
			</#if>
			<#assign pre=i>
		</#list>
		<#if (currentPage == totalPage)>
			<li class="disabled"><span>下页</span></li>
		<#else>
			<li><a href="javascript:void(0);"page="${currentPage + 1}">下页</a></li>
		</#if>
	</ul>
</#macro>