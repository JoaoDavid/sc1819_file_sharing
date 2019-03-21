<!DOCTYPE html>
<html class="" lang="en">
<head prefix="og: http://ogp.me/ns#">
<meta charset="utf-8">
<meta content="IE=edge" http-equiv="X-UA-Compatible">
<meta content="object" property="og:type">
<meta content="GitLab" property="og:site_name">
<meta content="SegC-V01/src/server/Skel.java · 602b6ba877c4293e120ec4993800e9d2928d6094 · João David / SegC-P1" property="og:title">
<meta content="Servidor de controlo de versões do alunos do Departamento de Informática" property="og:description">
<meta content="https://git.alunos.di.fc.ul.pt/assets/gitlab_logo-7ae504fe4f68fdebb3c2034e36621930cd36ea87924c11ff65dbcb8ed50dca58.png" property="og:image">
<meta content="64" property="og:image:width">
<meta content="64" property="og:image:height">
<meta content="https://git.alunos.di.fc.ul.pt/fc49448/segc-p1/blob/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server/Skel.java" property="og:url">
<meta content="summary" property="twitter:card">
<meta content="SegC-V01/src/server/Skel.java · 602b6ba877c4293e120ec4993800e9d2928d6094 · João David / SegC-P1" property="twitter:title">
<meta content="Servidor de controlo de versões do alunos do Departamento de Informática" property="twitter:description">
<meta content="https://git.alunos.di.fc.ul.pt/assets/gitlab_logo-7ae504fe4f68fdebb3c2034e36621930cd36ea87924c11ff65dbcb8ed50dca58.png" property="twitter:image">

<title>SegC-V01/src/server/Skel.java · 602b6ba877c4293e120ec4993800e9d2928d6094 · João David / SegC-P1 · GitLab</title>
<meta content="Servidor de controlo de versões do alunos do Departamento de Informática" name="description">
<link rel="shortcut icon" type="image/png" href="/uploads/-/system/appearance/favicon/1/logotipo1.png" id="favicon" data-original-href="/uploads/-/system/appearance/favicon/1/logotipo1.png" />
<link rel="stylesheet" media="all" href="/assets/application-e26a276298f24265784769e832caf976e30efdf8b813712fba20da91fbf0e405.css" />
<link rel="stylesheet" media="print" href="/assets/print-c8ff536271f8974b8a9a5f75c0ca25d2b8c1dceb4cff3c01d1603862a0bdcbfc.css" />



<script>
//<![CDATA[
window.gon={};gon.api_version="v4";gon.default_avatar_url="https://git.alunos.di.fc.ul.pt/assets/no_avatar-849f9c04a3a0d0cea2424ae97b27447dc64a7dbfae83c036c45b403392f0e8ba.png";gon.max_file_size=10;gon.asset_host=null;gon.webpack_public_path="/assets/webpack/";gon.relative_url_root="";gon.shortcuts_path="/help/shortcuts";gon.user_color_scheme="white";gon.gitlab_url="https://git.alunos.di.fc.ul.pt";gon.revision="ce13864";gon.gitlab_logo="/assets/gitlab_logo-7ae504fe4f68fdebb3c2034e36621930cd36ea87924c11ff65dbcb8ed50dca58.png";gon.sprite_icons="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg";gon.sprite_file_icons="/assets/file_icons-7262fc6897e02f1ceaf8de43dc33afa5e4f9a2067f4f68ef77dcc87946575e9e.svg";gon.emoji_sprites_css_path="/assets/emoji_sprites-289eccffb1183c188b630297431be837765d9ff4aed6130cf738586fb307c170.css";gon.test_env=false;gon.suggested_label_colors=["#0033CC","#428BCA","#44AD8E","#A8D695","#5CB85C","#69D100","#004E00","#34495E","#7F8C8D","#A295D6","#5843AD","#8E44AD","#FFECDB","#AD4363","#D10069","#CC0033","#FF0000","#D9534F","#D1D100","#F0AD4E","#AD8D43"];gon.first_day_of_week=0;gon.current_user_id=438;gon.current_username="fc49448";gon.current_user_fullname="João David";gon.current_user_avatar_url="/uploads/-/system/user/avatar/438/avatar.png";
//]]>
</script>


<script src="/assets/webpack/runtime.2758e43e.bundle.js" defer="defer"></script>
<script src="/assets/webpack/main.587b17f4.chunk.js" defer="defer"></script>
<script src="/assets/webpack/commons~pages.groups~pages.groups.activity~pages.groups.boards~pages.groups.clusters.destroy~pages.g~99a59a9e.dd9cf9fb.chunk.js" defer="defer"></script>
<script src="/assets/webpack/commons~pages.groups.milestones.edit~pages.groups.milestones.new~pages.projects.blame.show~pages.pro~bedd5722.087ab757.chunk.js" defer="defer"></script>
<script src="/assets/webpack/pages.projects.blob.show.a6638448.chunk.js" defer="defer"></script>
<script>
  window.uploads_path = "/fc49448/segc-p1/uploads";
</script>

<meta name="csrf-param" content="authenticity_token" />
<meta name="csrf-token" content="/NRVNPH3O+DTyqJGvhChJ4v/0Z+usQj8FxTduRYGyfci3uO8ywlfAA7pq/sldAwXJtR3FRRF9wYz0FVkRwTC4g==" />
<meta content="origin-when-cross-origin" name="referrer">
<meta content="width=device-width, initial-scale=1, maximum-scale=1" name="viewport">
<meta content="#474D57" name="theme-color">
<link rel="apple-touch-icon" type="image/x-icon" href="/assets/touch-icon-iphone-5a9cee0e8a51212e70b90c87c12f382c428870c0ff67d1eb034d884b78d2dae7.png" />
<link rel="apple-touch-icon" type="image/x-icon" href="/assets/touch-icon-ipad-a6eec6aeb9da138e507593b464fdac213047e49d3093fc30e90d9a995df83ba3.png" sizes="76x76" />
<link rel="apple-touch-icon" type="image/x-icon" href="/assets/touch-icon-iphone-retina-72e2aadf86513a56e050e7f0f2355deaa19cc17ed97bbe5147847f2748e5a3e3.png" sizes="120x120" />
<link rel="apple-touch-icon" type="image/x-icon" href="/assets/touch-icon-ipad-retina-8ebe416f5313483d9c1bc772b5bbe03ecad52a54eba443e5215a22caed2a16a2.png" sizes="152x152" />
<link color="rgb(226, 67, 41)" href="/assets/logo-d36b5212042cebc89b96df4bf6ac24e43db316143e89926c0db839ff694d2de4.svg" rel="mask-icon">
<meta content="/assets/msapplication-tile-1196ec67452f618d39cdd85e2e3a542f76574c071051ae7effbfde01710eb17d.png" name="msapplication-TileImage">
<meta content="#30353E" name="msapplication-TileColor">



</head>

<body class="ui-indigo  gl-browser-chrome gl-platform-windows" data-find-file="/fc49448/segc-p1/find_file/602b6ba877c4293e120ec4993800e9d2928d6094" data-group="" data-page="projects:blob:show" data-project="segc-p1">

<script>
  gl = window.gl || {};
  gl.client = {"isChrome":true,"isWindows":true};
</script>


<header class="navbar navbar-gitlab qa-navbar navbar-expand-sm js-navbar">
<a class="sr-only gl-accessibility" href="#content-body" tabindex="1">Skip to content</a>
<div class="container-fluid">
<div class="header-content">
<div class="title-container">
<h1 class="title">
<a title="Dashboard" id="logo" href="/"><img data-src="/uploads/-/system/appearance/header_logo/1/logotipo.png" class="lazy" src="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==" />
</a></h1>
<ul class="list-unstyled navbar-sub-nav">
<li id="nav-projects-dropdown" class="home dropdown header-projects qa-projects-dropdown" data-track-label="projects_dropdown" data-track-event="click_dropdown"><button data-toggle="dropdown" type="button">
Projects
<svg class="caret-down"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#angle-down"></use></svg>
</button>
<div class="dropdown-menu frequent-items-dropdown-menu">
<div class="frequent-items-dropdown-container">
<div class="frequent-items-dropdown-sidebar qa-projects-dropdown-sidebar">
<ul>
<li class=""><a class="qa-your-projects-link" href="/dashboard/projects">Your projects
</a></li><li class=""><a href="/dashboard/projects/starred">Starred projects
</a></li><li class=""><a href="/explore">Explore projects
</a></li></ul>
</div>
<div class="frequent-items-dropdown-content">
<div data-project-id="3024" data-project-name="SegC-P1" data-project-namespace="João David / SegC-P1" data-project-web-url="/fc49448/segc-p1" data-user-name="fc49448" id="js-projects-dropdown"></div>
</div>
</div>

</div>
</li><li id="nav-groups-dropdown" class="home dropdown header-groups qa-groups-dropdown" data-track-label="groups_dropdown" data-track-event="click_dropdown"><button data-toggle="dropdown" type="button">
Groups
<svg class="caret-down"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#angle-down"></use></svg>
</button>
<div class="dropdown-menu frequent-items-dropdown-menu">
<div class="frequent-items-dropdown-container">
<div class="frequent-items-dropdown-sidebar qa-groups-dropdown-sidebar">
<ul>
<li class=""><a class="qa-your-groups-link" href="/dashboard/groups">Your groups
</a></li><li class=""><a href="/explore/groups">Explore groups
</a></li></ul>
</div>
<div class="frequent-items-dropdown-content">
<div data-user-name="fc49448" id="js-groups-dropdown"></div>
</div>
</div>

</div>
</li><li class="d-none d-xl-block d-lg-block"><a class="dashboard-shortcuts-activity" title="Activity" href="/dashboard/activity">Activity
</a></li><li class="d-none d-xl-block d-lg-block"><a class="dashboard-shortcuts-milestones" title="Milestones" href="/dashboard/milestones">Milestones
</a></li><li class="d-none d-xl-block d-lg-block"><a class="dashboard-shortcuts-snippets" title="Snippets" href="/dashboard/snippets">Snippets
</a></li><li class="d-lg-none d-xl-none dropdown header-more">
<a data-toggle="dropdown" href="#">
More
<svg class="caret-down"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#angle-down"></use></svg>
</a>
<div class="dropdown-menu">
<ul>
<li class=""><a title="Activity" href="/dashboard/activity">Activity
</a></li><li class=""><a class="dashboard-shortcuts-milestones" title="Milestones" href="/dashboard/milestones">Milestones
</a></li><li class=""><a class="dashboard-shortcuts-snippets" title="Snippets" href="/dashboard/snippets">Snippets
</a></li>
</ul>
</div>
</li>
<li class="hidden">
<a title="Projects" class="dashboard-shortcuts-projects" href="/dashboard/projects">Projects
</a></li>

</ul>

</div>
<div class="navbar-collapse collapse">
<ul class="nav navbar-nav">
<li class="header-new dropdown" data-track-event="click_dropdown" data-track-label="new_dropdown">
<a class="header-new-dropdown-toggle has-tooltip qa-new-menu-toggle" title="New..." ref="tooltip" aria-label="New..." data-toggle="dropdown" data-placement="bottom" data-container="body" data-display="static" href="/projects/new"><svg class="s16"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#plus-square"></use></svg>
<svg class="caret-down"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#angle-down"></use></svg>
</a><div class="dropdown-menu dropdown-menu-right">
<ul>
<li class="dropdown-bold-header">
This project
</li>
<li><a href="/fc49448/segc-p1/issues/new">New issue</a></li>
<li><a href="/fc49448/segc-p1/merge_requests/new">New merge request</a></li>
<li><a href="/fc49448/segc-p1/snippets/new">New snippet</a></li>
<li class="divider"></li>
<li class="dropdown-bold-header">GitLab</li>
<li><a class="qa-global-new-project-link" href="/projects/new">New project</a></li>
<li><a href="/groups/new">New group</a></li>
<li><a href="/snippets/new">New snippet</a></li>
</ul>
</div>
</li>

<li class="nav-item d-none d-sm-none d-md-block m-auto">
<div class="search search-form" data-track-event="activate_form_input" data-track-label="navbar_search">
<form class="form-inline" action="/search" accept-charset="UTF-8" method="get"><input name="utf8" type="hidden" value="&#x2713;" /><div class="search-input-container">
<div class="search-input-wrap">
<div class="dropdown" data-url="/search/autocomplete">
<input type="search" name="search" id="search" placeholder="Search or jump to…" class="search-input dropdown-menu-toggle no-outline js-search-dashboard-options" spellcheck="false" tabindex="1" autocomplete="off" data-issues-path="/dashboard/issues" data-mr-path="/dashboard/merge_requests" aria-label="Search or jump to…" />
<button class="hidden js-dropdown-search-toggle" data-toggle="dropdown" type="button"></button>
<div class="dropdown-menu dropdown-select">
<div class="dropdown-content"><ul>
<li class="dropdown-menu-empty-item">
<a>
Loading...
</a>
</li>
</ul>
</div><div class="dropdown-loading"><i aria-hidden="true" data-hidden="true" class="fa fa-spinner fa-spin"></i></div>
</div>
<svg class="s16 search-icon"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#search"></use></svg>
<svg class="s16 clear-icon js-clear-input"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#close"></use></svg>
</div>
</div>
</div>
<input type="hidden" name="group_id" id="group_id" class="js-search-group-options" />
<input type="hidden" name="project_id" id="search_project_id" value="3024" class="js-search-project-options" data-project-path="segc-p1" data-name="SegC-P1" data-issues-path="/fc49448/segc-p1/issues" data-mr-path="/fc49448/segc-p1/merge_requests" data-issues-disabled="false" />
<input type="hidden" name="search_code" id="search_code" value="true" />
<input type="hidden" name="repository_ref" id="repository_ref" value="602b6ba877c4293e120ec4993800e9d2928d6094" />

<div class="search-autocomplete-opts hide" data-autocomplete-path="/search/autocomplete" data-autocomplete-project-id="3024" data-autocomplete-project-ref="602b6ba877c4293e120ec4993800e9d2928d6094"></div>
</form></div>

</li>
<li class="nav-item d-inline-block d-sm-none d-md-none">
<a title="Search" aria-label="Search" data-toggle="tooltip" data-placement="bottom" data-container="body" href="/search?project_id=3024"><svg class="s16"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#search"></use></svg>
</a></li>
<li class="user-counter"><a title="Issues" class="dashboard-shortcuts-issues" aria-label="Issues" data-toggle="tooltip" data-placement="bottom" data-container="body" href="/dashboard/issues?assignee_username=fc49448"><svg class="s16"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#issues"></use></svg>
<span class="badge badge-pill hidden issues-count">
0
</span>
</a></li><li class="user-counter"><a title="Merge requests" class="dashboard-shortcuts-merge_requests" aria-label="Merge requests" data-toggle="tooltip" data-placement="bottom" data-container="body" href="/dashboard/merge_requests?assignee_username=fc49448"><svg class="s16"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#git-merge"></use></svg>
<span class="badge badge-pill hidden merge-requests-count">
0
</span>
</a></li><li class="user-counter"><a title="Todos" aria-label="Todos" class="shortcuts-todos" data-toggle="tooltip" data-placement="bottom" data-container="body" href="/dashboard/todos"><svg class="s16"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#todo-done"></use></svg>
<span class="badge badge-pill hidden todos-count">
0
</span>
</a></li><li class="nav-item header-help dropdown">
<a class="header-help-dropdown-toggle" data-toggle="dropdown" href="/help"><svg class="s16"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#question"></use></svg>
<svg class="caret-down"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#angle-down"></use></svg>
</a><div class="dropdown-menu dropdown-menu-right">
<ul>
<li>
<a href="/help">Help</a>
</li>
<li class="divider"></li>
<li>
<a href="https://about.gitlab.com/submit-feedback">Submit feedback</a>
</li>
<li>
<a target="_blank" class="text-nowrap" href="https://about.gitlab.com/contributing">Contribute to GitLab
</a></li>

</ul>

</div>
</li>
<li class="nav-item header-user dropdown" data-track-event="click_dropdown" data-track-label="profile_dropdown">
<a class="header-user-dropdown-toggle" data-toggle="dropdown" href="/fc49448"><img width="23" height="23" class="header-user-avatar qa-user-avatar lazy" data-src="/uploads/-/system/user/avatar/438/avatar.png?width=23" src="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==" />
<svg class="caret-down"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#angle-down"></use></svg>
</a><div class="dropdown-menu dropdown-menu-right">
<ul>
<li class="current-user">
<div class="user-name bold">
João David
</div>
@fc49448
</li>
<li class="divider"></li>
<li>
<div class="js-set-status-modal-trigger" data-has-status="false"></div>
</li>
<li>
<a class="profile-link" data-user="fc49448" href="/fc49448">Profile</a>
</li>
<li>
<a href="/profile">Settings</a>
</li>
<li class="divider"></li>
<li>
<a class="sign-out-link" href="/users/sign_out">Sign out</a>
</li>
</ul>

</div>
</li>
</ul>
</div>
<button class="navbar-toggler d-block d-sm-none" type="button">
<span class="sr-only">Toggle navigation</span>
<svg class="s12 more-icon js-navbar-toggle-right"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#ellipsis_h"></use></svg>
<svg class="s12 close-icon js-navbar-toggle-left"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#close"></use></svg>
</button>
</div>
</div>
</header>
<div class="js-set-status-modal-wrapper" data-current-emoji="" data-current-message=""></div>

<div class="layout-page page-with-contextual-sidebar">
<div class="nav-sidebar">
<div class="nav-sidebar-inner-scroll">
<div class="context-header">
<a title="SegC-P1" href="/fc49448/segc-p1"><div class="avatar-container s40 project-avatar">
<div class="avatar s40 avatar-tile identicon bg1">S</div>
</div>
<div class="sidebar-context-title">
SegC-P1
</div>
</a></div>
<ul class="sidebar-top-level-items">
<li class="home"><a class="shortcuts-project" href="/fc49448/segc-p1"><div class="nav-icon-container">
<svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#home"></use></svg>
</div>
<span class="nav-item-name">
Project
</span>
</a><ul class="sidebar-sub-level-items">
<li class="fly-out-top-item"><a href="/fc49448/segc-p1"><strong class="fly-out-top-item-name">
Project
</strong>
</a></li><li class="divider fly-out-top-item"></li>
<li class=""><a title="Project details" class="shortcuts-project" href="/fc49448/segc-p1"><span>Details</span>
</a></li><li class=""><a title="Activity" class="shortcuts-project-activity qa-activity-link" href="/fc49448/segc-p1/activity"><span>Activity</span>
</a></li><li class=""><a title="Releases" class="shortcuts-project-releases" href="/fc49448/segc-p1/releases"><span>Releases</span>
</a></li>
<li class=""><a title="Cycle Analytics" class="shortcuts-project-cycle-analytics" href="/fc49448/segc-p1/cycle_analytics"><span>Cycle Analytics</span>
</a></li></ul>
</li><li class="active"><a class="shortcuts-tree qa-project-menu-repo" href="/fc49448/segc-p1/tree/602b6ba877c4293e120ec4993800e9d2928d6094"><div class="nav-icon-container">
<svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#doc-text"></use></svg>
</div>
<span class="nav-item-name">
Repository
</span>
</a><ul class="sidebar-sub-level-items">
<li class="fly-out-top-item active"><a href="/fc49448/segc-p1/tree/602b6ba877c4293e120ec4993800e9d2928d6094"><strong class="fly-out-top-item-name">
Repository
</strong>
</a></li><li class="divider fly-out-top-item"></li>
<li class="active"><a href="/fc49448/segc-p1/tree/602b6ba877c4293e120ec4993800e9d2928d6094">Files
</a></li><li class=""><a href="/fc49448/segc-p1/commits/602b6ba877c4293e120ec4993800e9d2928d6094">Commits
</a></li><li class=""><a class="qa-branches-link" href="/fc49448/segc-p1/branches">Branches
</a></li><li class=""><a href="/fc49448/segc-p1/tags">Tags
</a></li><li class=""><a href="/fc49448/segc-p1/graphs/602b6ba877c4293e120ec4993800e9d2928d6094">Contributors
</a></li><li class=""><a href="/fc49448/segc-p1/network/602b6ba877c4293e120ec4993800e9d2928d6094">Graph
</a></li><li class=""><a href="/fc49448/segc-p1/compare?from=master&amp;to=602b6ba877c4293e120ec4993800e9d2928d6094">Compare
</a></li><li class=""><a href="/fc49448/segc-p1/graphs/602b6ba877c4293e120ec4993800e9d2928d6094/charts">Charts
</a></li>
</ul>
</li><li class=""><a class="shortcuts-issues qa-issues-item" href="/fc49448/segc-p1/issues"><div class="nav-icon-container">
<svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#issues"></use></svg>
</div>
<span class="nav-item-name">
Issues
</span>
<span class="badge badge-pill count issue_counter">
0
</span>
</a><ul class="sidebar-sub-level-items">
<li class="fly-out-top-item"><a href="/fc49448/segc-p1/issues"><strong class="fly-out-top-item-name">
Issues
</strong>
<span class="badge badge-pill count issue_counter fly-out-badge">
0
</span>
</a></li><li class="divider fly-out-top-item"></li>
<li class=""><a title="Issues" href="/fc49448/segc-p1/issues"><span>
List
</span>
</a></li><li class=""><a title="Board" href="/fc49448/segc-p1/boards"><span>
Board
</span>
</a></li><li class=""><a title="Labels" class="qa-labels-link" href="/fc49448/segc-p1/labels"><span>
Labels
</span>
</a></li>
<li class=""><a title="Milestones" class="qa-milestones-link" href="/fc49448/segc-p1/milestones"><span>
Milestones
</span>
</a></li></ul>
</li><li class=""><a class="shortcuts-merge_requests qa-merge-requests-link" href="/fc49448/segc-p1/merge_requests"><div class="nav-icon-container">
<svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#git-merge"></use></svg>
</div>
<span class="nav-item-name">
Merge Requests
</span>
<span class="badge badge-pill count merge_counter js-merge-counter">
0
</span>
</a><ul class="sidebar-sub-level-items is-fly-out-only">
<li class="fly-out-top-item"><a href="/fc49448/segc-p1/merge_requests"><strong class="fly-out-top-item-name">
Merge Requests
</strong>
<span class="badge badge-pill count merge_counter js-merge-counter fly-out-badge">
0
</span>
</a></li></ul>
</li><li class=""><a class="shortcuts-pipelines qa-link-pipelines" href="/fc49448/segc-p1/pipelines"><div class="nav-icon-container">
<svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#rocket"></use></svg>
</div>
<span class="nav-item-name">
CI / CD
</span>
</a><ul class="sidebar-sub-level-items">
<li class="fly-out-top-item"><a href="/fc49448/segc-p1/pipelines"><strong class="fly-out-top-item-name">
CI / CD
</strong>
</a></li><li class="divider fly-out-top-item"></li>
<li class=""><a title="Pipelines" class="shortcuts-pipelines" href="/fc49448/segc-p1/pipelines"><span>
Pipelines
</span>
</a></li><li class=""><a title="Jobs" class="shortcuts-builds" href="/fc49448/segc-p1/-/jobs"><span>
Jobs
</span>
</a></li><li class=""><a title="Schedules" class="shortcuts-builds" href="/fc49448/segc-p1/pipeline_schedules"><span>
Schedules
</span>
</a></li><li class=""><a title="Charts" class="shortcuts-pipelines-charts" href="/fc49448/segc-p1/pipelines/charts"><span>
Charts
</span>
</a></li></ul>
</li><li class=""><a class="shortcuts-operations qa-link-operations" href="/fc49448/segc-p1/environments/metrics"><div class="nav-icon-container">
<svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#cloud-gear"></use></svg>
</div>
<span class="nav-item-name">
Operations
</span>
</a><ul class="sidebar-sub-level-items">
<li class="fly-out-top-item"><a href="/fc49448/segc-p1/environments/metrics"><strong class="fly-out-top-item-name">
Operations
</strong>
</a></li><li class="divider fly-out-top-item"></li>
<li class=""><a title="Metrics" class="shortcuts-metrics" href="/fc49448/segc-p1/environments/metrics"><span>
Metrics
</span>
</a></li>
<li class=""><a title="Environments" class="shortcuts-environments qa-operations-environments-link" href="/fc49448/segc-p1/environments"><span>
Environments
</span>
</a></li><li class=""><a title="Error Tracking" class="shortcuts-tracking qa-operations-tracking-link" href="/fc49448/segc-p1/error_tracking"><span>
Error Tracking
</span>
</a></li><li class=""><a title="Serverless" href="/fc49448/segc-p1/serverless/functions"><span>
Serverless
</span>
</a></li><li class=""><a title="Kubernetes" class="shortcuts-kubernetes" href="/fc49448/segc-p1/clusters"><span>
Kubernetes
</span>
<div class="feature-highlight js-feature-highlight" data-container="body" data-dismiss-endpoint="/-/user_callouts" data-highlight="gke_cluster_integration" data-placement="right" data-toggle="popover" data-trigger="manual" disabled></div>
</a><div class="feature-highlight-popover-content">
<img class="feature-highlight-illustration lazy" data-src="/assets/illustrations/cluster_popover-9830388038d966d8d64d43576808f9d5ba05f639a78a40bae9a5ddc7cbf72f24.svg" src="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==" />
<div class="feature-highlight-popover-sub-content">
<p>Allows you to add and manage Kubernetes clusters.</p>
<p>
Protip:
<a href="/help/topics/autodevops/index.md">Auto DevOps</a>
<span>uses Kubernetes clusters to deploy your code!</span>
</p>
<hr>
<button class="btn btn-success btn-sm dismiss-feature-highlight" type="button">
<span>Got it!</span>
<svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#thumb-up"></use></svg>
</button>
</div>
</div>
</li></ul>
</li><li class=""><a class="shortcuts-wiki qa-wiki-link" href="/fc49448/segc-p1/wikis/home"><div class="nav-icon-container">
<svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#book"></use></svg>
</div>
<span class="nav-item-name">
Wiki
</span>
</a><ul class="sidebar-sub-level-items is-fly-out-only">
<li class="fly-out-top-item"><a href="/fc49448/segc-p1/wikis/home"><strong class="fly-out-top-item-name">
Wiki
</strong>
</a></li></ul>
</li><li class=""><a class="shortcuts-snippets" href="/fc49448/segc-p1/snippets"><div class="nav-icon-container">
<svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#snippet"></use></svg>
</div>
<span class="nav-item-name">
Snippets
</span>
</a><ul class="sidebar-sub-level-items is-fly-out-only">
<li class="fly-out-top-item"><a href="/fc49448/segc-p1/snippets"><strong class="fly-out-top-item-name">
Snippets
</strong>
</a></li></ul>
</li><li class=""><a class="shortcuts-tree" href="/fc49448/segc-p1/edit"><div class="nav-icon-container">
<svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#settings"></use></svg>
</div>
<span class="nav-item-name qa-settings-item">
Settings
</span>
</a><ul class="sidebar-sub-level-items">
<li class="fly-out-top-item"><a href="/fc49448/segc-p1/edit"><strong class="fly-out-top-item-name">
Settings
</strong>
</a></li><li class="divider fly-out-top-item"></li>
<li class=""><a title="General" href="/fc49448/segc-p1/edit"><span>
General
</span>
</a></li><li class=""><a title="Members" class="qa-link-members-settings" href="/fc49448/segc-p1/project_members"><span>
Members
</span>
</a></li><li class=""><a title="Integrations" href="/fc49448/segc-p1/settings/integrations"><span>
Integrations
</span>
</a></li><li class=""><a title="Repository" href="/fc49448/segc-p1/settings/repository"><span>
Repository
</span>
</a></li><li class=""><a title="CI / CD" href="/fc49448/segc-p1/settings/ci_cd"><span>
CI / CD
</span>
</a></li><li class=""><a title="Operations" href="/fc49448/segc-p1/settings/operations">Operations
</a></li>
</ul>
</li><a class="toggle-sidebar-button js-toggle-sidebar" role="button" title="Toggle sidebar" type="button">
<svg class="icon-angle-double-left"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#angle-double-left"></use></svg>
<svg class="icon-angle-double-right"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#angle-double-right"></use></svg>
<span class="collapse-text">Collapse sidebar</span>
</a>
<button name="button" type="button" class="close-nav-button"><svg class="s16"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#close"></use></svg>
<span class="collapse-text">Close sidebar</span>
</button>
<li class="hidden">
<a title="Activity" class="shortcuts-project-activity" href="/fc49448/segc-p1/activity"><span>
Activity
</span>
</a></li>
<li class="hidden">
<a title="Network" class="shortcuts-network" href="/fc49448/segc-p1/network/602b6ba877c4293e120ec4993800e9d2928d6094">Graph
</a></li>
<li class="hidden">
<a title="Charts" class="shortcuts-repository-charts" href="/fc49448/segc-p1/graphs/602b6ba877c4293e120ec4993800e9d2928d6094/charts">Charts
</a></li>
<li class="hidden">
<a class="shortcuts-new-issue" href="/fc49448/segc-p1/issues/new">Create a new issue
</a></li>
<li class="hidden">
<a title="Jobs" class="shortcuts-builds" href="/fc49448/segc-p1/-/jobs">Jobs
</a></li>
<li class="hidden">
<a title="Commits" class="shortcuts-commits" href="/fc49448/segc-p1/commits/602b6ba877c4293e120ec4993800e9d2928d6094">Commits
</a></li>
<li class="hidden">
<a title="Issue Boards" class="shortcuts-issue-boards" href="/fc49448/segc-p1/boards">Issue Boards</a>
</li>
</ul>
</div>
</div>

<div class="content-wrapper">

<div class="mobile-overlay"></div>
<div class="alert-wrapper">




<nav class="breadcrumbs container-fluid container-limited" role="navigation">
<div class="breadcrumbs-container">
<button name="button" type="button" class="toggle-mobile-nav"><span class="sr-only">Open sidebar</span>
<i aria-hidden="true" data-hidden="true" class="fa fa-bars"></i>
</button><div class="breadcrumbs-links js-title-container">
<ul class="list-unstyled breadcrumbs-list js-breadcrumbs-list">
<li><a href="/fc49448">João David</a><svg class="s8 breadcrumbs-list-angle"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#angle-right"></use></svg></li> <li><a href="/fc49448/segc-p1"><span class="breadcrumb-item-text js-breadcrumb-item-text">SegC-P1</span></a><svg class="s8 breadcrumbs-list-angle"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#angle-right"></use></svg></li>

<li>
<h2 class="breadcrumbs-sub-title"><a href="/fc49448/segc-p1/blob/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server/Skel.java">Repository</a></h2>
</li>
</ul>
</div>

</div>
</nav>

<div class="flash-container flash-container-page">
</div>

<div class="d-flex"></div>
</div>
<div class=" ">
<div class="content" id="content-body">
<div class="js-signature-container" data-signatures-path="/fc49448/segc-p1/commits/98ed9a3b9f6c011c470026e72261fa73defd2398/signatures"></div>
<div class="container-fluid container-limited">

<div class="tree-holder" id="tree-holder">
<div class="nav-block">
<div class="tree-ref-container">
<div class="tree-ref-holder">
<form class="project-refs-form" action="/fc49448/segc-p1/refs/switch" accept-charset="UTF-8" method="get"><input name="utf8" type="hidden" value="&#x2713;" /><input type="hidden" name="destination" id="destination" value="blob" />
<input type="hidden" name="path" id="path" value="SegC-V01/src/server/Skel.java" />
<div class="dropdown">
<button class="dropdown-menu-toggle js-project-refs-dropdown qa-branches-select" type="button" data-toggle="dropdown" data-selected="602b6ba877c4293e120ec4993800e9d2928d6094" data-ref="602b6ba877c4293e120ec4993800e9d2928d6094" data-refs-url="/fc49448/segc-p1/refs?sort=updated_desc" data-field-name="ref" data-submit-form-on-click="true" data-visit="true"><span class="dropdown-toggle-text ">602b6ba877c4293e120ec4993800e9d2928d6094</span><i aria-hidden="true" data-hidden="true" class="fa fa-chevron-down"></i></button>
<div class="dropdown-menu dropdown-menu-paging dropdown-menu-selectable git-revision-dropdown qa-branches-dropdown">
<div class="dropdown-page-one">
<div class="dropdown-title"><span>Switch branch/tag</span><button class="dropdown-title-button dropdown-menu-close" aria-label="Close" type="button"><i aria-hidden="true" data-hidden="true" class="fa fa-times dropdown-menu-close-icon"></i></button></div>
<div class="dropdown-input"><input type="search" id="" class="dropdown-input-field" placeholder="Search branches and tags" autocomplete="off" /><i aria-hidden="true" data-hidden="true" class="fa fa-search dropdown-input-search"></i><i aria-hidden="true" data-hidden="true" role="button" class="fa fa-times dropdown-input-clear js-dropdown-input-clear"></i></div>
<div class="dropdown-content"></div>
<div class="dropdown-loading"><i aria-hidden="true" data-hidden="true" class="fa fa-spinner fa-spin"></i></div>
</div>
</div>
</div>
</form>
</div>
<ul class="breadcrumb repo-breadcrumb">
<li class="breadcrumb-item">
<a href="/fc49448/segc-p1/tree/602b6ba877c4293e120ec4993800e9d2928d6094">segc-p1
</a></li>
<li class="breadcrumb-item">
<a href="/fc49448/segc-p1/tree/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01">SegC-V01</a>
</li>
<li class="breadcrumb-item">
<a href="/fc49448/segc-p1/tree/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src">src</a>
</li>
<li class="breadcrumb-item">
<a href="/fc49448/segc-p1/tree/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server">server</a>
</li>
<li class="breadcrumb-item">
<a href="/fc49448/segc-p1/blob/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server/Skel.java"><strong>Skel.java</strong>
</a></li>
</ul>
</div>
<div class="tree-controls">
<a class="btn shortcuts-find-file" rel="nofollow" href="/fc49448/segc-p1/find_file/602b6ba877c4293e120ec4993800e9d2928d6094"><i aria-hidden="true" data-hidden="true" class="fa fa-search"></i>
<span>Find file</span>
</a>
<div class="btn-group" role="group"><a class="btn js-blob-blame-link" href="/fc49448/segc-p1/blame/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server/Skel.java">Blame</a><a class="btn" href="/fc49448/segc-p1/commits/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server/Skel.java">History</a><a class="btn js-data-file-blob-permalink-url" href="/fc49448/segc-p1/blob/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server/Skel.java">Permalink</a></div>
</div>
</div>

<div class="info-well d-none d-sm-block">
<div class="well-segment">
<ul class="blob-commit-info">
<li class="commit flex-row js-toggle-container" id="commit-98ed9a3b">
<div class="avatar-cell d-none d-sm-block">
<a href="/fc49038"><img alt="João Miguel Antunes Marques&#39;s avatar" src="https://secure.gravatar.com/avatar/0a7212b6e7eb87f493e4a4694005ab33?s=72&amp;d=identicon" class="avatar s36 d-none d-sm-inline" title="João Miguel Antunes Marques" /></a>
</div>
<div class="commit-detail flex-list">
<div class="commit-content qa-commit-content">
<a class="commit-row-message item-title" href="/fc49448/segc-p1/commit/98ed9a3b9f6c011c470026e72261fa73defd2398">del of semaphore;</a>
<span class="commit-row-message d-block d-sm-none">
&middot;
98ed9a3b
</span>
<button class="text-expander js-toggle-button">
<svg class="s12"><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#ellipsis_h"></use></svg>
</button>
<div class="committer">
<a class="commit-author-link js-user-link" data-user-id="407" href="/fc49038">João Miguel Antunes Marques</a> authored <time class="js-timeago" title="Mar 10, 2019 9:05pm" datetime="2019-03-10T21:05:58Z" data-toggle="tooltip" data-placement="bottom" data-container="body">Mar 10, 2019</time>
</div>
<pre class="commit-row-description js-toggle-content append-bottom-8">add information to log</pre>
</div>
<div class="commit-actions flex-row d-none d-sm-flex">

<div class="js-commit-pipeline-status" data-endpoint="/fc49448/segc-p1/commit/98ed9a3b9f6c011c470026e72261fa73defd2398/pipelines?ref=602b6ba877c4293e120ec4993800e9d2928d6094"></div>
<div class="commit-sha-group">
<div class="label label-monospace">
98ed9a3b
</div>
<button class="btn btn btn-default" data-toggle="tooltip" data-placement="bottom" data-container="body" data-title="Copy commit SHA to clipboard" data-class="btn btn-default" data-clipboard-text="98ed9a3b9f6c011c470026e72261fa73defd2398" type="button" title="Copy commit SHA to clipboard" aria-label="Copy commit SHA to clipboard"><svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#duplicate"></use></svg></button>

</div>
</div>
</div>
</li>

</ul>
</div>


</div>
<div class="blob-content-holder" id="blob-content-holder">
<article class="file-holder">
<div class="js-file-title file-title-flex-parent">
<div class="file-header-content">
<i aria-hidden="true" data-hidden="true" class="fa fa-file-text-o fa-fw"></i>
<strong class="file-title-name">
Skel.java
</strong>
<button class="btn btn-clipboard btn-transparent prepend-left-5" data-toggle="tooltip" data-placement="bottom" data-container="body" data-class="btn-clipboard btn-transparent prepend-left-5" data-title="Copy file path to clipboard" data-clipboard-text="{&quot;text&quot;:&quot;SegC-V01/src/server/Skel.java&quot;,&quot;gfm&quot;:&quot;`SegC-V01/src/server/Skel.java`&quot;}" type="button" title="Copy file path to clipboard" aria-label="Copy file path to clipboard"><svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#duplicate"></use></svg></button>
<small>
5.17 KB
</small>
</div>

<div class="file-actions">

<div class="btn-group" role="group"><button class="btn btn btn-sm js-copy-blob-source-btn" data-toggle="tooltip" data-placement="bottom" data-container="body" data-class="btn btn-sm js-copy-blob-source-btn" data-title="Copy source to clipboard" data-clipboard-target=".blob-content[data-blob-id=&#39;73a730a0ba1e8edadeb58408907a144e483a7e3f&#39;]" type="button" title="Copy source to clipboard" aria-label="Copy source to clipboard"><svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#duplicate"></use></svg></button><a class="btn btn-sm has-tooltip" target="_blank" rel="noopener noreferrer" title="Open raw" data-container="body" href="/fc49448/segc-p1/raw/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server/Skel.java"><i aria-hidden="true" data-hidden="true" class="fa fa-file-code-o"></i></a><a download="SegC-V01/src/server/Skel.java" class="btn btn-sm has-tooltip" target="_blank" rel="noopener noreferrer" title="Download" data-container="body" href="/fc49448/segc-p1/raw/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server/Skel.java?inline=false"><svg><use xlink:href="/assets/icons-24aaa921aa9e411162e6913688816c79861d0de4bee876cf6fc4c794be34ee91.svg#download"></use></svg></a></div>
<div class="btn-group" role="group"><button name="button" type="submit" class="btn js-edit-blob  disabled has-tooltip" title="You can only edit files when you are on a branch" data-container="body">Edit</button><button name="button" type="submit" class="btn btn-default disabled has-tooltip" title="You can only edit files when you are on a branch" data-container="body">Web IDE</button><button name="button" type="submit" class="btn btn-default disabled has-tooltip" title="You can only replace files when you are on a branch" data-container="body">Replace</button><button name="button" type="submit" class="btn btn-remove disabled has-tooltip" title="You can only delete files when you are on a branch" data-container="body">Delete</button></div>
</div>
</div>
<div class="js-file-fork-suggestion-section file-fork-suggestion hidden">
<span class="file-fork-suggestion-note">
You're not allowed to
<span class="js-file-fork-suggestion-section-action">
edit
</span>
files in this project directly. Please fork this project,
make your changes there, and submit a merge request.
</span>
<a class="js-fork-suggestion-button btn btn-grouped btn-inverted btn-success" rel="nofollow" data-method="post" href="/fc49448/segc-p1/blob/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server/Skel.java">Fork</a>
<button class="js-cancel-fork-suggestion-button btn btn-grouped" type="button">
Cancel
</button>
</div>



<div class="blob-viewer" data-type="simple" data-url="/fc49448/segc-p1/blob/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server/Skel.java?format=json&amp;viewer=simple">
<div class="text-center prepend-top-default append-bottom-default">
<i aria-hidden="true" aria-label="Loading content…" class="fa fa-spinner fa-spin fa-2x"></i>
</div>

</div>


</article>
</div>

<div class="modal" id="modal-upload-blob">
<div class="modal-dialog modal-lg">
<div class="modal-content">
<div class="modal-header">
<h3 class="page-title">Replace Skel.java</h3>
<button aria-label="Close" class="close" data-dismiss="modal" type="button">
<span aria-hidden="true">&times;</span>
</button>
</div>
<div class="modal-body">
<form class="js-quick-submit js-upload-blob-form" data-method="put" action="/fc49448/segc-p1/update/602b6ba877c4293e120ec4993800e9d2928d6094/SegC-V01/src/server/Skel.java" accept-charset="UTF-8" method="post"><input name="utf8" type="hidden" value="&#x2713;" /><input type="hidden" name="_method" value="put" /><input type="hidden" name="authenticity_token" value="5y105pvsX9DqdcxsmXlqRIys6kqaUg8n15O0Dm2miUE5J8JuoRI7MDdWxdECHcd0IYdMwCCm8N3zVzzTPKSCVA==" /><div class="dropzone">
<div class="dropzone-previews blob-upload-dropzone-previews">
<p class="dz-message light">
Attach a file by drag &amp; drop or <a class="markdown-selector" href="#">click to upload</a>
</p>
</div>
</div>
<br>
<div class="dropzone-alerts alert alert-danger data" style="display:none"></div>
<div class="form-group row commit_message-group">
<label class="col-form-label col-sm-2" for="commit_message-b9a1ae0a5686cab81e1326d807165f0b">Commit message
</label><div class="col-sm-10">
<div class="commit-message-container">
<div class="max-width-marker"></div>
<textarea name="commit_message" id="commit_message-b9a1ae0a5686cab81e1326d807165f0b" class="form-control js-commit-message" placeholder="Replace Skel.java" required="required" rows="3">
Replace Skel.java</textarea>
</div>
</div>
</div>

<div class="form-group row branch">
<label class="col-form-label col-sm-2" for="branch_name">Target Branch</label>
<div class="col-sm-10">
<input type="text" name="branch_name" id="branch_name" required="required" class="form-control js-branch-name ref-name" />
<div class="js-create-merge-request-container">
<div class="form-check prepend-top-8">
<input type="checkbox" name="create_merge_request" id="create_merge_request-6339a3de1788348cb80a0e4924339252" value="1" class="js-create-merge-request form-check-input" checked="checked" />
<label class="form-check-label" for="create_merge_request-6339a3de1788348cb80a0e4924339252">Start a <strong>new merge request</strong> with these changes
</label></div>

</div>
</div>
</div>
<input type="hidden" name="original_branch" id="original_branch" value="602b6ba877c4293e120ec4993800e9d2928d6094" class="js-original-branch" />

<div class="form-actions">
<button name="button" type="button" class="btn btn-success btn-upload-file" id="submit-all"><i aria-hidden="true" data-hidden="true" class="fa fa-spin fa-spinner js-loading-icon hidden"></i>
Replace file
</button><a class="btn btn-cancel" data-dismiss="modal" href="#">Cancel</a>

</div>
</form></div>
</div>
</div>
</div>

</div>
</div>

</div>
</div>
</div>
</div>


</body>
</html>

