
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>

<%@ page import="com.clubsport.model.ClassementCommune"%>

 

<%

  List<String> regions = (List<String>) request.getAttribute("regions");

  String regionSel = (String) request.getAttribute("region");

 

  List<String> federations = (List<String>) request.getAttribute("federations");

  String fedSel = (String) request.getAttribute("codeFederation");

 

  String nomCommuneSel = (String) request.getAttribute("nomCommune");

 

  List<ClassementCommune> classement =

      (List<ClassementCommune>) request.getAttribute("classement");

 

  String ctx = request.getContextPath();

 

  StringBuilder labelsCommunes = new StringBuilder("[");

  StringBuilder dataLicencies = new StringBuilder("[");

 

  if (classement != null) {

 

      for (int i = 0; i < classement.size(); i++) {

 

          ClassementCommune cc = classement.get(i);

 

          labelsCommunes.append("\"")

              .append(cc.getNomCommune().replace("\"", "\\\""))

              .append("\"");

 

          dataLicencies.append(cc.getTotalLicencies());

 

          if (i < classement.size() - 1) {

              labelsCommunes.append(",");

              dataLicencies.append(",");

          }

      }

  }

 

  labelsCommunes.append("]");

  dataLicencies.append("]");

%>

 

<!DOCTYPE html>

<html>

<head>

   
<meta charset="UTF-8">

   
<title>Visualisation</title>    
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	        rel="stylesheet">

   
<link rel="stylesheet"
	        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

   
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

   
<style>
 

    body {      background:#f8f9fa;      color:#1e293b;   
	
}

 

    .stat-card,     .main-card {      border:none;      border-radius:14px; 
	    background:white;   
	
}
 





 
</style>

 

</head>

 

<body>

	 

	<div class="container-fluid p-4">

		   
		<!-- Retour -->

		  <a href="<%= ctx %>/elu"
			     class="btn btn-outline-secondary btn-sm mb-3 rounded-pill">

			      <i class="fas fa-arrow-left me-1"></i>     Retour au dashboard
			élu    
		</a>    
		<!-- Titre -->

		 
		<h1 class="h2 fw-bold mb-1">      Visualisation    </h1>

		   
		<p class="text-muted mb-4">      Statistiques et classements des
			licenciés par territoire.    </p>

		   
		<!-- Erreur -->

		 
		<% if (request.getAttribute("erreur") != null) { %>

		     
		<div class="alert alert-danger">

			        <%= request.getAttribute("erreur") %>

			     
		</div>

		   
		<% } %>

		   
		<!-- FILTRES -->

		 
		<form method="get"         action="<%= ctx %>/elu/visualisation"
			        class="card main-card shadow-sm p-4 mb-4">

			     
			<div class="row g-3">

				        
				<!-- Région -->

				     
				<div class="col-md-3">

					          <label class="form-label fw-semibold">          
						  Région           </label>           <select class="form-select"
						                name="region">            
						<option value="">              Toutes les régions        
							   </option>             <% if (regions != null) {

 

               for (String r : regions) { %>              
						<option value="<%= r %>"
							              <%= r.equals(regionSel) ? "selected" : "" %>>

							                <%= r %>              
						</option>             <% }

             } %>          
					</select>        
				</div>

				        
				<!-- Fédération -->

				     
				<div class="col-md-3">

					          <label class="form-label fw-semibold">          
						  Fédération           </label>           <select class="form-select"
						                name="codeFederation">            
						<option value="">              Toutes les fédérations    
							       </option>             <% if (federations != null) {

 

               for (String f : federations) {

 

                 String code = f;

 

                 int pos = f.indexOf(" — ");

 

                 if (pos == -1)

                     pos = f.indexOf(" - ");

 

                 if (pos != -1)

                     code = f.substring(0, pos).trim();

          %>              
						<option value="<%= code %>"
							              <%= code.equals(fedSel) ? "selected" : "" %>>

							                <%= f %>              
						</option>             <% }

             } %>          
					</select>        
				</div>

				        
				<!-- Commune -->

				     
				<div class="col-md-3">

					          <label class="form-label fw-semibold">          
						  Commune           </label>           <input class="form-control"
						               type="text"                name="nomCommune"
						               placeholder="ex. Rouen"
						               value="<%= nomCommuneSel != null ? nomCommuneSel : "" %>">

					       
				</div>

				        
				<!-- Boutons -->

				     
				<div class="col-md-3 d-flex align-items-end gap-2">

					         
					<button type="submit"                 class="btn btn-primary w-100">

						            Filtrer          </button>

					          <a href="<%= ctx %>/elu/visualisation"
						           class="btn btn-outline-secondary w-100">          
						  Réinitialiser           </a>        
				</div>

				     
			</div>

			   
		</form>

		   
		<!-- STATISTIQUES -->

		 
		<div class="row g-3 mb-4">

			      
			<!-- Total -->

			   
			<div class="col-md-4">

				       
				<div class="card stat-card shadow-sm text-center p-4">

					         
					<div class="fs-2 fw-bold">

						            <%= String.format("%,d",

              request.getAttribute("total") != null

              ? ((Number) request.getAttribute("total")).longValue()

              : 0L).replace(",", " ") %>

						         
					</div>

					         
					<div class="text-muted">            Total licenciés          
					</div>

					       
				</div>

				     
			</div>

			      
			<!-- Hommes -->

			   
			<div class="col-md-4">

				       
				<div class="card stat-card shadow-sm text-center p-4">

					         
					<div class="fs-2 fw-bold text-primary">

						            <%= String.format("%,d",

              request.getAttribute("totalHommes") != null

              ? ((Number) request.getAttribute("totalHommes")).longValue()

              : 0L).replace(",", " ") %>

						         
					</div>

					         
					<div class="text-muted">            Hommes          </div>

					       
				</div>

				     
			</div>

			      
			<!-- Femmes -->

			   
			<div class="col-md-4">

				       
				<div class="card stat-card shadow-sm text-center p-4">

					         
					<div class="fs-2 fw-bold text-warning">

						            <%= String.format("%,d",

              request.getAttribute("totalFemmes") != null

              ? ((Number) request.getAttribute("totalFemmes")).longValue()

              : 0L).replace(",", " ") %>

						         
					</div>

					         
					<div class="text-muted">            Femmes          </div>

					       
				</div>

				     
			</div>

			   
		</div>

		   
		<!-- GRAPHIQUES -->

		 
		<div class="row g-4 mb-4">

			      
			<!-- Pie -->

			   
			<div class="col-md-5">

				       
				<div class="card main-card shadow-sm p-4">

					         
					<h2 class="h6 fw-bold mb-3">            Répartition Hommes /
						Femmes          </h2>

					         
					<canvas id="chartHF"></canvas>

					       
				</div>

				     
			</div>

			      
			<!-- Histogramme -->

			    <% if (classement != null && !classement.isEmpty()) { %>

			       
			<div class="col-md-7">

				         
				<div class="card main-card shadow-sm p-4">

					           
					<h2 class="h6 fw-bold mb-3">              Nombre de licenciés
						par commune            </h2>

					           
					<canvas id="chartClassement"
						                  style="max-height: 350px;">

          </canvas>

					         
				</div>

				       
			</div>

			      <% } %>

			   
		</div>

		   
		<!-- TABLEAU -->

		 
		<% if (classement != null && !classement.isEmpty()) { %>

		     
		<div class="card main-card shadow-sm p-4 mt-4">

			       
			<h2 class="h6 fw-bold mb-3">          Classement des communes
				par nombre de licenciés        </h2>

			       
			<table class="table table-bordered table-sm">

				         
				<thead class="table-light">

					           
					<tr>

						             
						<th>Rang</th>            
						<th>Commune</th>            
						<th>Licenciés</th>            
						<th>Taux (%)</th>            
					</tr>

					         
				</thead>

				         
				<tbody>

					            <% for (int i = 0; i < classement.size(); i++) {

 

               ClassementCommune cc = classement.get(i);

          %>

					             
					<tr>

						               
						<td>                  <%= i + 1 %>                
						</td>                
						<td>                  <%= cc.getNomCommune() %>              
							 
						</td>                
						<td>                  <%= String.format("%,d",

                    ((Number) cc.getTotalLicencies()).longValue()

                ).replace(",", " ") %>                
						</td>                
						<td>                  <%= String.format("%.2f",

                    cc.getTauxLicencies()) %> %                
						</td>              
					</tr>

					            <% } %>

					         
				</tbody>

				       
			</table>

			     
		</div>

		   
		<% } else { %>

		     
		<div class="alert alert-info mt-4">        Aucune commune
			trouvée pour cette région/fédération.      </div>

		   
		<% } %>

		   
		<!-- EXPORT PDF -->

		 
		<button type="button"           class="btn btn-danger mt-3"
			          onclick="exportChartsPDF()">      Exporter les
			graphiques PDF    </button>

		   
		<form id="exportPdfForm"         method="post"
			        action="<%= ctx %>/elu/export-pdf">

			      <input type="hidden"            name="region"
				           value="<%= regionSel != null ? regionSel : "" %>">

			      <input type="hidden"            name="codeFederation"
				           value="<%= fedSel != null ? fedSel : "" %>">    
			  <input type="hidden"            name="nomCommune"
				           value="<%= nomCommuneSel != null ? nomCommuneSel : "" %>">

			      <input type="hidden"            id="chartHFImage"
				           name="chartHF">       <input type="hidden"
				           id="chartClassementImage"
				           name="chartClassement">    
		</form>

		 

	</div>

	 

	<script>
		 

  const totalHommes =
	<%= request.getAttribute("totalHommes") != null

        ? request.getAttribute("totalHommes")

        : 0 %>
		;

		const totalFemmes =
	<%= request.getAttribute("totalFemmes") != null

        ? request.getAttribute("totalFemmes")

        : 0 %>
		;

		// PIE CHART

		new Chart(document.getElementById('chartHF'), {

			type : 'pie',

			data : {

				labels : [ 'Hommes', 'Femmes' ],

				datasets : [ {

					data : [ totalHommes, totalFemmes ],

					backgroundColor : [ '#4e79a7', '#f28e2b' ]

				} ]

			},

			options : {

				plugins : {

					legend : {

						position : 'bottom'

					}

				}

			}

		});
	<% if (classement != null && !classement.isEmpty()) { %>
		const labelsCommunes =
	<%= labelsCommunes %>
		;

		const dataLicencies =
	<%= dataLicencies %>
		;

		// BAR CHART

		new Chart(document.getElementById('chartClassement'), {

			type : 'bar',

			data : {

				labels : labelsCommunes,

				datasets : [ {

					label : 'Nombre de licenciés',

					data : dataLicencies,

					backgroundColor : '#4e79a7'

				} ]

			},

			options : {

				indexAxis : 'y',

				plugins : {

					legend : {

						display : false

					}

				},

				scales : {

					x : {

						beginAtZero : true

					}

				}

			}

		});
	<% } %>
		// EXPORT PDF

		function exportChartsPDF() {

			const chartHF =

			document.getElementById("chartHF");

			const chartClassement =

			document.getElementById("chartClassement");

			document.getElementById("chartHFImage").value =

			chartHF.toDataURL("image/png");

			if (chartClassement) {

				document.getElementById("chartClassementImage").value =

				chartClassement.toDataURL("image/png");

			}

			document.getElementById("exportPdfForm").submit();

		}
	</script>

	 

</body>

</html>

