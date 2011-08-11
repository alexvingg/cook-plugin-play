# Routes
# This file defines all application routes (Higher priority routes first)
# ~~~~

# Home page
GET     /                                       ${controller}.index


# Rotas Padrão
GET     /${r"{"}controller}                           ${r"{"}controller}.index
GET     /${r"{"}controller}/view/${r"{"}id}                 ${r"{"}controller}.view
GET     /${r"{"}controller}/form/${r"{"}id}                 ${r"{"}controller}.form
GET     /${r"{"}controller}/delete/${r"{"}id}               ${r"{"}controller}.delete
POST    /${r"{"}controller}/save/${r"{"}id}                 ${r"{"}controller}.save

# Map static resources from the /app/public folder to the /public path
GET     /public/                                staticDir:public

# Catch all
*       /${r"{"}controller}/${r"{"}action}                  ${r"{"}controller}.${r"{"}action}
*       /${r"{"}controller}/${r"{"}action}/${r"{"}id}             ${r"{"}controller}.${r"{"}action}
