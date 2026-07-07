(function() {
    document.addEventListener('DOMContentLoaded', function() {
        var maxThemes = 3;
        var themesCheckboxes = document.querySelectorAll('input[name="themes"]');

        if (!themesCheckboxes.length) {
            return;
        }

        function updateThemesState() {
            var checkedCount = Array.prototype.filter.call(themesCheckboxes, function(checkbox) {
                return checkbox.checked;
            }).length;

            themesCheckboxes.forEach(function(checkbox) {
                checkbox.disabled = !checkbox.checked && checkedCount >= maxThemes;
            });
        }

        themesCheckboxes.forEach(function(checkbox) {
            checkbox.addEventListener('change', updateThemesState);
        });

        updateThemesState();
    });
})();