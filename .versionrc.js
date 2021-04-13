
module.exports = function() {
  var hostUrl = "https://github.com/"
  var projectUrl = hostUrl + "VerstSiu/frame_pager"

  return {
    types: [
      {type: "feat", section: "新功能"},
      {type: "fix", section: "问题修复"},
      {type: "chore", section: "杂项", hidden: true},
      {type: "docs", section: "文档更新", hidden: true},
      {type: "style", section: "样式调整"},
      {type: "refactor", section: "代码重构"},
      {type: "perf", section: "性能优化"},
      {type: "test", section: "测试更新", hidden: true},
      {type: "ci", section: "CI配置", hidden: true},
      {type: "revert", section: "回退", hidden: true}
    ],
    preMajor: true,
    bumpFiles: [
        {
          filename: "publish-config.gradle",
          updater: "publish-config-updater.js"
        },
        {
          filename: "package.json",
          type: "json"
        }
      ],

    commitUrlFormat: projectUrl + "commit/{{hash}}",
    compareUrlFormat: projectUrl + "compare/{{previousTag}}...{{currentTag}}",
    issueUrlFormat: projectUrl + "issues/{{id}}",
    userUrlFormat: hostUrl + "{{user}}"
  };
}
