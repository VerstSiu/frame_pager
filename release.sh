#!/bin/bash
init_file="./npm-init.iml"
if [ ! -f "$init_file" ]; then
    # 安装 standard-version 至本地目录，加快命令的执行速度
    npm i --save-dev standard-version
    touch "$init_file"
    echo -e "\033[32m ======== 'npm install' 初始化完成 ========= \033[0m"
fi

echo "选择发布的版本类型："
std_option=''
select version in "alpha" "beta" "rc" "release"
do
    case $version in
    "alpha")
    std_option='--prerelease alpha'
    ;;
    "beta")
    std_option='--prerelease beta'
    ;;
    "rc")
    std_option='--prerelease rc'
    ;;
    "release")
    # shellcheck disable=SC2089
    std_option='--release'
    ;;
    *)
    echo "非法输入！" ; exit
    ;;
    esac

    echo "当前选中 : $version"
    break
done

no_release=false
read -r -n1 -p "是否使用 dry-run 模式运行[y/N]? " pick_option
echo ""
if [[ $pick_option == "n" || $pick_option == "N" || $pick_option == "" ]]; then
    std_suffix=""
else
    std_suffix="--dry-run"
    no_release=true
fi

std_command="npx standard-version -t \"\" ${std_option} ${std_suffix}"
echo "最终命令为：$std_command 尝试执行..."
$std_command

if [ $no_release == true ]; then
    echo "dry-run 运行结束，退出脚本"
    exit 0
fi

read -r -n1 -p "是否推送分支到远程仓库（执行 git push）[Y/n]? " pick_option
echo ""
if [[ $pick_option == "y" || $pick_option == "Y" || $pick_option == "" ]]; then
    printf "\n 推送分支到远程仓库...\n"
    git push
else
    exit 0
fi

## 推送tag
LAST_TAG=$(git for-each-ref --format '%(tag)' --sort=-taggerdate --count=1)
read -r -n1 -p "是否推送最新 tag: ${LAST_TAG} [Y/n]? " pick_option
echo ""
if [[ $pick_option == "y" || $pick_option == "Y" || $pick_option == "" ]]; then
    git push origin "${LAST_TAG}"
fi