let g:project_root="~/Projects/karel"
source ~/.vimrc_generic_project
let paths_list = ['src/**' ]
call SetSourcePaths(paths_list)
set grepprg=~/bin/pose_grep.sh\ $*
